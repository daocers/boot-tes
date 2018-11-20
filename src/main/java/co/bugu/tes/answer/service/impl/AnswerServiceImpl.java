package co.bugu.tes.answer.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.answer.dao.AnswerDao;
import co.bugu.tes.answer.domain.Answer;
import co.bugu.tes.answer.service.IAnswerService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class AnswerServiceImpl implements IAnswerService {
    @Autowired
    AnswerDao answerDao;

    private Logger logger = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(Answer answer) {
        //todo 校验参数
        answer.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        answer.setCreateTime(now);
        answer.setUpdateTime(now);
        answerDao.insert(answer);
        return answer.getId();
    }

    @Override
    public int updateById(Answer answer) {
        logger.debug("answer updateById, 参数： {}", JSON.toJSONString(answer, true));
        Preconditions.checkNotNull(answer.getId(), "id不能为空");
        answer.setUpdateTime(new Date());
        return answerDao.updateById(answer);
    }

    @Override
    public List<Answer> findByCondition(Answer answer) {
        logger.debug("answer findByCondition, 参数： {}", JSON.toJSONString(answer, true));
        PageHelper.orderBy(ORDER_BY);
        List<Answer> answers = answerDao.findByObject(answer);

        logger.debug("查询结果， {}", JSON.toJSONString(answers, true));
        return answers;
    }

    @Override
    public List<Answer> findByCondition(Integer pageNum, Integer pageSize, Answer answer) {
        logger.debug("answer findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(answer, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Answer> answers = answerDao.findByObject(answer);

        logger.debug("查询结果， {}", JSON.toJSONString(answers, true));
        return answers;
    }

    @Override
    public PageInfo<Answer> findByConditionWithPage(Integer pageNum, Integer pageSize, Answer answer) {
        logger.debug("answer findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(answer, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Answer> answers = answerDao.findByObject(answer);

        logger.debug("查询结果， {}", JSON.toJSONString(answers, true));
        return new PageInfo<>(answers);
    }

    @Override
    public Answer findById(Long answerId) {
        logger.debug("answer findById, 参数 answerId: {}", answerId);
        Answer answer = answerDao.selectById(answerId);

        logger.debug("查询结果： {}", JSON.toJSONString(answer, true));
        return answer;
    }

    @Override
    public int deleteById(Long answerId, Long operatorId) {
        logger.debug("answer 删除， 参数 answerId : {}", answerId);
        Answer answer = new Answer();
        answer.setId(answerId);
        answer.setIsDel(DelFlagEnum.YES.getCode());
        answer.setUpdateTime(new Date());
        answer.setUpdateUserId(operatorId);
        int num = answerDao.updateById(answer);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
