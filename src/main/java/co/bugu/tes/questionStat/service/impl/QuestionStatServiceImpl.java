package co.bugu.tes.questionStat.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.questionStat.dao.QuestionStatDao;
import co.bugu.tes.questionStat.domain.QuestionStat;
import co.bugu.tes.questionStat.service.IQuestionStatService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author daocers
 * @create 2019-05-09 10:49
 */
@Service
public class QuestionStatServiceImpl implements IQuestionStatService {
    @Autowired
    QuestionStatDao questionStatDao;

    //    上一次处理的最大answerId
    private Long lastAnswerId;

    private static Logger logger = LoggerFactory.getLogger(QuestionStatServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(QuestionStat questionStat) {
        //todo 校验参数
        questionStat.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        questionStat.setCreateTime(now);
        questionStat.setUpdateTime(now);
        questionStatDao.insert(questionStat);
        return questionStat.getId();
    }

    @Override
    public int updateById(QuestionStat questionStat) {
        logger.debug("questionStat updateById, 参数： {}", JSON.toJSONString(questionStat, true));
        Preconditions.checkNotNull(questionStat.getId(), "id不能为空");
        questionStat.setUpdateTime(new Date());
        return questionStatDao.updateById(questionStat);
    }

    @Override
    public List<QuestionStat> findByCondition(QuestionStat questionStat) {
        logger.debug("questionStat findByCondition, 参数： {}", JSON.toJSONString(questionStat, true));
        PageHelper.orderBy(ORDER_BY);
        List<QuestionStat> questionStats = questionStatDao.findByObject(questionStat);

        logger.debug("查询结果， {}", JSON.toJSONString(questionStats, true));
        return questionStats;
    }

    @Override
    public List<QuestionStat> findByCondition(Integer pageNum, Integer pageSize, QuestionStat questionStat) {
        logger.debug("questionStat findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(questionStat, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<QuestionStat> questionStats = questionStatDao.findByObject(questionStat);

        logger.debug("查询结果， {}", JSON.toJSONString(questionStats, true));
        return questionStats;
    }

    @Override
    public PageInfo<QuestionStat> findByConditionWithPage(Integer pageNum, Integer pageSize, QuestionStat questionStat) {
        logger.debug("questionStat findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(questionStat, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<QuestionStat> questionStats = questionStatDao.findByObject(questionStat);

        logger.debug("查询结果， {}", JSON.toJSONString(questionStats, true));
        return new PageInfo<>(questionStats);
    }

    @Override
    public QuestionStat findById(Long questionStatId) {
        logger.debug("questionStat findById, 参数 questionStatId: {}", questionStatId);
        QuestionStat questionStat = questionStatDao.selectById(questionStatId);

        logger.debug("查询结果： {}", JSON.toJSONString(questionStat, true));
        return questionStat;
    }

    @Override
    public int deleteById(Long questionStatId, Long operatorId) {
        logger.debug("questionStat 删除， 参数 questionStatId : {}", questionStatId);
        QuestionStat questionStat = new QuestionStat();
        questionStat.setId(questionStatId);
        questionStat.setIsDel(DelFlagEnum.YES.getCode());
        questionStat.setUpdateTime(new Date());
        questionStat.setUpdateUserId(operatorId);
        int num = questionStatDao.updateById(questionStat);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

    @Override
    public boolean getAnswerAndProcessStat(int size) {
        if (null == lastAnswerId) {
            List<QuestionStat> questionStats = findByCondition(1, 1, new QuestionStat());
            if (CollectionUtils.isEmpty(questionStats)) {
                lastAnswerId = 0L;
            } else {
                lastAnswerId = questionStats.get(0).getLastAnswerId();
            }
        }
        Long endAnswerId = lastAnswerId + size;
        List<QuestionStat> questionStats = questionStatDao.getAnswerStat(lastAnswerId, endAnswerId);
        while (CollectionUtils.isEmpty(questionStats)) {
            logger.info("没有新增数据，lastAnswerId: {}", lastAnswerId);
            return true;
        }

        if (CollectionUtils.isNotEmpty(questionStats)) {
            Long tmpAnswerId = questionStats.get(0).getId();
            Map<Long, QuestionStat> map = new HashMap<>();
            Map<Long, Map<String, Integer>> answerMap = new HashMap<>();
            for (QuestionStat questionStat : questionStats) {
                Long questionId = questionStat.getQuestionId();
                Integer questionType = questionStat.getQuestionType();
                int delta = questionStat.getTotal();
                String answer = questionStat.getAnswer();
                String rightAnswer = questionStat.getRightAnswer();


                QuestionStat stat = map.get(questionId);
                if (null == stat) {
                    stat = new QuestionStat();
                    stat.setQuestionId(questionId);
                    stat.setQuestionType(questionType);
                    stat.setTotal(0);
                    stat.setWrongCount(0);
                    stat.setRightCount(0);
                    stat.setWrongRate(0.00);
                    stat.setLastAnswerId(tmpAnswerId);
                }
                stat.setTotal(stat.getTotal() + delta);
                if (rightAnswer.equals(answer)) {
                    stat.setRightCount(stat.getRightCount() + delta);
                } else {
                    stat.setWrongCount(stat.getWrongCount() + delta);
                }
                stat.setWrongRate(((double) stat.getWrongCount()) / stat.getTotal() * 100);
                map.put(questionId, stat);



                Map<String, Integer> answerInfo = answerMap.get(questionId);
                if (answerInfo == null) {
                    answerInfo = new HashMap<>();
                }
                answerInfo.put(answer, answerInfo.containsKey(answer) ? answerInfo.get(answer) + delta : delta);

                answerMap.put(questionId, answerInfo);

            }

            Set<Long> questionIds = map.keySet();
            if (CollectionUtils.isNotEmpty(questionIds)) {
                for (Long questionId : questionIds) {
                    QuestionStat stat = map.get(questionId);
                    stat.setCreateUserId(0L);
                    stat.setUpdateUserId(0L);
                    stat.setIsDel(DelFlagEnum.NO.getCode());

                    List<QuestionStat> list = findByCondition(1, 1, stat);
                    if (CollectionUtils.isEmpty(list)) {
                        questionStatDao.insert(stat);
                    } else {
                        String answerRecord = list.get(0).getAnswerRecord();
                        Map<String, Integer> recordMap = JSON.parseObject(answerRecord, Map.class);
                        Map<String, Integer> answerInfoMap = answerMap.get(questionId);

                        Set<String> answerItemSet = answerInfoMap.keySet();
                        if (CollectionUtils.isNotEmpty(answerItemSet)) {
                            for (String answerItem : answerItemSet) {
                                if (recordMap.containsKey(answerItem)) {
                                    recordMap.put(answerItem, recordMap.get(answerItem) + answerInfoMap.get(answerItem));
                                } else {
                                    recordMap.put(answerItem, answerInfoMap.get(answerItem));
                                }
                            }
                        }
                        stat.setAnswerRecord(JSON.toJSONString(recordMap));
                        stat.setId(list.get(0).getId());
                        questionStatDao.updateById(stat);
                    }
                }
            }


//            更新最大答案id
            lastAnswerId = tmpAnswerId;
        }
        return true;
    }

}
