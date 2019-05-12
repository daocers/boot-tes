package co.bugu.tes.paper.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.answer.dao.AnswerDao;
import co.bugu.tes.answer.domain.Answer;
import co.bugu.tes.answer.dto.AnswerDto4GenPaper;
import co.bugu.tes.paper.dao.PaperDao;
import co.bugu.tes.paper.dao.vo.SceneMonitorVo;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.enums.AnswerFlagEnum;
import co.bugu.tes.paper.enums.PaperStatusEnum;
import co.bugu.tes.paper.enums.QuestionTypeEnum;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.scene.dto.SceneMonitorDto;
import co.bugu.tes.scene.service.ISceneService;
import co.bugu.util.CodeUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class PaperServiceImpl implements IPaperService {
    @Autowired
    PaperDao paperDao;
    @Autowired
    AnswerDao answerDao;
    @Autowired
    ISceneService sceneService;

    private Logger logger = LoggerFactory.getLogger(PaperServiceImpl.class);

    private static String ORDER_BY = "id desc";

    @Override
    public long add(Paper paper) {
        //todo 校验参数
        paper.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        paper.setCreateTime(now);
        paper.setUpdateTime(now);
        paperDao.insert(paper);
        return paper.getId();
    }

    @Override
    public int updateById(Paper paper) {
        logger.debug("paper updateById, 参数： {}", JSON.toJSONString(paper, true));
        Preconditions.checkNotNull(paper.getId(), "id不能为空");
        paper.setUpdateTime(new Date());
        return paperDao.updateById(paper);
    }

    @Override
    public List<Paper> findByCondition(Paper paper) {
        logger.debug("paper findByCondition, 参数： {}", JSON.toJSONString(paper, true));
        PageHelper.orderBy(ORDER_BY);
        List<Paper> papers = paperDao.findByObject(paper);

        logger.debug("查询结果， {}", JSON.toJSONString(papers, true));
        return papers;
    }

    @Override
    public List<Paper> findByCondition(Integer pageNum, Integer pageSize, Paper paper) {
        logger.debug("paper findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(paper, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Paper> papers = paperDao.findByObject(paper);

        logger.debug("查询结果， {}", JSON.toJSONString(papers, true));
        return papers;
    }

    @Override
    public PageInfo<Paper> findByConditionWithPage(Integer pageNum, Integer pageSize, Paper paper) {
        logger.debug("paper findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(paper, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Paper> papers = paperDao.findByObject(paper);

        logger.debug("查询结果， {}", JSON.toJSONString(papers, true));
        return new PageInfo<>(papers);
    }

    @Override
    public Paper findById(Long paperId) {
        logger.debug("paper findById, 参数 paperId: {}", paperId);
        Paper paper = paperDao.selectById(paperId);

        logger.debug("查询结果： {}", JSON.toJSONString(paper, true));
        return paper;
    }

    @Override
    public int deleteById(Long paperId, Long operatorId) {
        logger.debug("paper 删除， 参数 paperId : {}", paperId);
        Paper paper = new Paper();
        paper.setId(paperId);
        paper.setIsDel(DelFlagEnum.YES.getCode());
        paper.setUpdateTime(new Date());
        paper.setUpdateUserId(operatorId);
        int num = paperDao.updateById(paper);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 3000)
    public Long createPaper(Long sceneId, Long userId, List<AnswerDto4GenPaper> sIds, List<AnswerDto4GenPaper> mIds, List<AnswerDto4GenPaper> jIds) {
        Date now = new Date();
        Paper paper = new Paper();
        paper.setSceneId(sceneId);
        paper.setUserId(userId);
        paper.setCreateTime(now);
        paper.setUpdateTime(now);
        paper.setIsDel(DelFlagEnum.NO.getCode());
        paper.setStatus(PaperStatusEnum.OK.getCode());
        paper.setCreateUserId(userId);
        paper.setUpdateUserId(userId);
        paper.setAnswerFlag(AnswerFlagEnum.NO.getCode());
        paper.setBeginTime(now);
        paper.setCode(CodeUtil.getPaperCode());

        paperDao.insert(paper);
        Long paperId = paper.getId();

        List<Answer> answers = new ArrayList<>();
        for (AnswerDto4GenPaper dto : sIds) {
            Answer answer = new Answer();
            answer.setSceneId(sceneId);
            answer.setIsDel(DelFlagEnum.NO.getCode());
            answer.setPaperId(paperId);
            answer.setCreateUserId(userId);
            answer.setUpdateUserId(userId);
            answer.setUserId(userId);
            answer.setCreateTime(now);
            answer.setUpdateTime(now);
            answer.setQuestionId(dto.getId());
            answer.setRightAnswer(dto.getAnswer());
            answer.setQuestionType(QuestionTypeEnum.SINGLE.getCode());
            answers.add(answer);
        }

        for (AnswerDto4GenPaper dto : mIds) {
            Answer answer = new Answer();
            answer.setSceneId(sceneId);
            answer.setIsDel(DelFlagEnum.NO.getCode());
            answer.setPaperId(paperId);
            answer.setCreateUserId(userId);
            answer.setUpdateUserId(userId);
            answer.setUserId(userId);
            answer.setCreateTime(now);
            answer.setUpdateTime(now);
            answer.setQuestionId(dto.getId());
            answer.setRightAnswer(dto.getAnswer());
            answer.setQuestionType(QuestionTypeEnum.MULTI.getCode());
            answers.add(answer);
        }

        for (AnswerDto4GenPaper dto : jIds) {
            Answer answer = new Answer();
            answer.setSceneId(sceneId);
            answer.setIsDel(DelFlagEnum.NO.getCode());
            answer.setPaperId(paperId);
            answer.setCreateUserId(userId);
            answer.setUpdateUserId(userId);
            answer.setUserId(userId);
            answer.setCreateTime(now);
            answer.setUpdateTime(now);
            answer.setQuestionId(dto.getId());
            answer.setRightAnswer(dto.getAnswer());
            answer.setQuestionType(QuestionTypeEnum.JUDGE.getCode());
            answers.add(answer);
        }

        answerDao.batchAdd(answers);
        return paperId;
    }

    @Override
    public List<Paper> getSceneScoreStat(int size) {
        List<Paper> papers = paperDao.getSceneScoreStat(size);
        return papers;
    }

    @Override
    public SceneMonitorDto getSceneMonitor(Long sceneId) {
        List<SceneMonitorVo> list = paperDao.getSceneMonitor(sceneId);
        SceneMonitorDto dto = new SceneMonitorDto();
        dto.setPaperCount(0);
        dto.setPaperCommitCount(0);
        dto.setPaperNoneCommitCount(0);
        dto.setId(sceneId);
        dto.setCommitRate(0);
        if (CollectionUtils.isEmpty(list)) {
            return dto;
        }
        for (SceneMonitorVo vo : list) {
            int status = vo.getStatus();
            int count = vo.getCount();
            if (PaperStatusEnum.CANCELED.getCode() == status) {
                continue;
            }
//            作答中
            if (PaperStatusEnum.OK.getCode() == status) {
                dto.setPaperNoneCommitCount(dto.getPaperNoneCommitCount() + count);
            } else {
                dto.setPaperCommitCount(dto.getPaperCommitCount() + count);
            }
            dto.setPaperCount(dto.getPaperCount() + count);
        }
        dto.setCommitRate(dto.getPaperCommitCount() * 100 / dto.getPaperCount());
        return dto;
    }


}
