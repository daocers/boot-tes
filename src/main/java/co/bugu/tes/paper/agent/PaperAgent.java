package co.bugu.tes.paper.agent;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.answer.domain.Answer;
import co.bugu.tes.answer.dto.AnswerDto4GenPaper;
import co.bugu.tes.answer.service.IAnswerService;
import co.bugu.tes.exam.dto.QuestionDto;
import co.bugu.tes.judge.domain.Judge;
import co.bugu.tes.judge.service.IJudgeService;
import co.bugu.tes.multi.domain.Multi;
import co.bugu.tes.multi.service.IMultiService;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.enums.AnswerFlagEnum;
import co.bugu.tes.paper.enums.PaperStatusEnum;
import co.bugu.tes.paper.enums.QuestionTypeEnum;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.service.ISceneService;
import co.bugu.tes.single.domain.Single;
import co.bugu.tes.single.service.ISingleService;
import co.bugu.util.UserUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author daocers
 * @Date 2018/11/25:20:42
 * @Description:
 */
@Service
public class PaperAgent {
    private Logger logger = LoggerFactory.getLogger(PaperAgent.class);
    @Autowired
    ISingleService singleService;
    @Autowired
    IMultiService multiService;
    @Autowired
    IJudgeService judgeService;

    @Autowired
    ISceneService sceneService;

    @Autowired
    IPaperService paperService;
    @Autowired
    IAnswerService answerService;


    /**
     * 通过answer对象获取试题信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/25 20:50
     */
    public QuestionDto getQuestionDto(Answer answer) {
        Long questionId = answer.getQuestionId();
        Integer questionType = answer.getQuestionType();
        QuestionDto dto = new QuestionDto();
        dto.setQuestionType(questionType);
        dto.setAnswerId(answer.getId());
//        如果本题已经做答，返显做答记录
        dto.setRealAnswer(answer.getAnswer());
        if (QuestionTypeEnum.SINGLE.getCode() == questionType) {
            Single single = singleService.findById(questionId);
            BeanUtils.copyProperties(single, dto);
        } else if (QuestionTypeEnum.MULTI.getCode() == questionType) {
            Multi multi = multiService.findById(questionId);
            BeanUtils.copyProperties(multi, dto);
        } else if (QuestionTypeEnum.JUDGE.getCode() == questionType) {
            Judge judge = judgeService.findById(questionId);
            BeanUtils.copyProperties(judge, dto);
        }
        return dto;
    }

    /**
     * 生成个人试卷
     *
     * @param
     * @return 试卷id
     * @auther daocers
     * @date 2018/11/25 21:06
     */
    public Long generatePaper(Long sceneId, Long userId) throws Exception {
        Preconditions.checkArgument(null != sceneId);
        Preconditions.checkArgument(null != userId);
        Scene scene = sceneService.findById(sceneId);
        if (scene == null) {
            throw new Exception("场次信息不存在");
        }
        Integer singleCount = scene.getSingleCount();
        Integer multiCount = scene.getMultiCount();
        Integer judgeCount = scene.getJudgeCount();
        Long bankId = scene.getQuestionBankId();
        Long paperPolicyId = scene.getPaperPolicyId();
        Integer paperGenerateType = scene.getPaperGenerateType();

        List<AnswerDto4GenPaper> sIds = null;
        List<AnswerDto4GenPaper> mIds = null;
        List<AnswerDto4GenPaper> jIds = null;
        if (paperPolicyId > 0) {
//            策略出题
            sIds = getQuestionIdListByPolicy(paperPolicyId, paperGenerateType, bankId, QuestionTypeEnum.SINGLE.getCode(), singleCount);
            mIds = getQuestionIdListByPolicy(paperPolicyId, paperGenerateType, bankId, QuestionTypeEnum.MULTI.getCode(), multiCount);
            jIds = getQuestionIdListByPolicy(paperPolicyId, paperGenerateType, bankId, QuestionTypeEnum.JUDGE.getCode(), judgeCount);
        } else {
//            简单出题

            sIds = getQuestionIdListInSimple(paperGenerateType, bankId, QuestionTypeEnum.SINGLE.getCode(), singleCount);
            mIds = getQuestionIdListInSimple(paperGenerateType, bankId, QuestionTypeEnum.MULTI.getCode(), multiCount);
            jIds = getQuestionIdListInSimple(paperGenerateType, bankId, QuestionTypeEnum.JUDGE.getCode(), judgeCount);
        }

        Long paperId = paperService.createPaper(sceneId, userId, sIds, mIds, jIds);
        return paperId;

    }

    /**
     * 简单模式出题
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/25 21:36
     */
    private List<AnswerDto4GenPaper> getQuestionIdListInSimple(Integer paperGenerateType, Long bankId, Integer questionType, Integer count) throws Exception {
//      TODO  出卷方式待完善   1 随机， 2 统一，3 统一乱序
        List<AnswerDto4GenPaper> ids = null;
        if (questionType == QuestionTypeEnum.SINGLE.getCode()) {
            Single single = new Single();
            single.setBankId(bankId);
            ids = singleService.getAllIds(single);
        } else if (questionType == QuestionTypeEnum.MULTI.getCode()) {
            Multi multi = new Multi();
            multi.setBankId(bankId);
            ids = multiService.getAllIds(multi);
        } else if (questionType == QuestionTypeEnum.JUDGE.getCode()) {
            Judge judge = new Judge();
            judge.setBankId(bankId);
            ids = judgeService.getAllIds(judge);
        }

        if (ids.size() < count) {
            throw new Exception("试题数量不足");
        }

        Collections.shuffle(ids);
        List<AnswerDto4GenPaper> list = ids.subList(0, count);
        return list;

    }


    /**
     * 策略模式出题
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/25 21:37
     */
    private List<AnswerDto4GenPaper> getQuestionIdListByPolicy(Long paperPolicyId, Integer paperGenerateType, Long bankId, Integer questionType, Integer count) {
//        todo 策略模式待开发
        return null;
    }

    /**
     * 提交试卷
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/27 15:54
     */
    @Transactional(rollbackFor = Exception.class, timeout = 3000)
    public void commitPaper(Long paperId, List<QuestionDto> questionDtos) throws Exception {
        Date now = new Date();
        Long userId = UserUtil.getCurrentUser().getId();
        Paper paper = paperService.findById(paperId);
        if (null == paper) {
            throw new Exception("试卷不存在");
        }

        paper.setStatus(PaperStatusEnum.COMMITED.getCode());
        paper.setUpdateUserId(userId);
        paper.setUpdateTime(now);
        paper.setAnswerFlag(AnswerFlagEnum.YES.getCode());
        paper.setEndTime(now);
        paperService.updateById(paper);

        for (QuestionDto dto : questionDtos) {
            Answer answer = new Answer();
            answer.setId(dto.getAnswerId());
            answer.setAnswer(dto.getRealAnswer());
            answer.setTimeLeft(dto.getLeftTimeInfo());
            answer.setUpdateTime(now);
            answerService.updateById(answer);
        }
    }

    /**
     * 提交单个试题
     *
     * @param
     * @return 修改了多少条数据
     * @auther daocers
     * @date 2018/11/27 16:50
     */
    public Integer commitQuestion(QuestionDto dto) {
        Answer answer = new Answer();
        answer.setId(dto.getAnswerId());
        answer.setAnswer(dto.getRealAnswer());
        answer.setTimeLeft(dto.getLeftTimeInfo());
        answer.setUpdateTime(new Date());
        int num = answerService.updateById(answer);
        return num;
    }


    /**
     * 计算得分
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/27 23:28
     */
    public Double computeScore(Scene scene, Paper paper) throws Exception {
        Answer query = new Answer();
        query.setPaperId(paper.getId());
        query.setIsDel(DelFlagEnum.NO.getCode());
        List<Answer> answers = answerService.findByCondition(query);

        if (CollectionUtils.isEmpty(answers)) {
            throw new Exception("没有找到该试卷的答案信息");
        }

        int singleCount = scene.getSingleCount();
        int multiCount = scene.getMultiCount();
        int judgeCount = scene.getJudgeCount();
        Double singleScore = scene.getSingleScore();
        Double multiScore = scene.getMultiScore();
        Double judgeScore = scene.getJudgeScore();
        int percentable = scene.getPercentable();

        if (answers.size() != singleCount + multiCount + judgeCount) {
            logger.warn("试题数量不符合场次要求， paperId: {}, sceneId: {}", new Object[]{paper.getId(), scene.getId()});
        }

        int sCount = 0;
        int mCount = 0;
        int jCount = 0;

        for (Answer answer : answers) {
//            答对了
            if (answer.getRightAnswer().equals(answer.getAnswer())) {
                if (answer.getQuestionType() == QuestionTypeEnum.SINGLE.getCode()) {
                    sCount++;
                } else if (answer.getQuestionType() == QuestionTypeEnum.MULTI.getCode()) {
                    mCount++;
                } else if (answer.getQuestionType() == QuestionTypeEnum.JUDGE.getCode()) {
                    jCount++;
                }
            }
        }
        double yourScore = (singleScore * 10 * sCount + multiScore * 10 * mCount + judgeScore * 10 * jCount) / 10;
        double totalScore = (singleScore * 10 * singleCount + multiScore * 10 * multiCount + judgeScore * 10 * judgeCount) / 10;
        double score = yourScore;
        if (percentable == 1) {
//            需要百分比
            score = score / totalScore * 100;
        }
        paper.setOriginalScore(totalScore);
        paper.setStatus(PaperStatusEnum.MARKED.getCode());
        paper.setUpdateTime(new Date());
        paper.setScore(score);
        int num = paperService.updateById(paper);
        if (num == 1) {
            return score;
        } else {
            throw new Exception("计算成绩失败");
        }
    }
}
