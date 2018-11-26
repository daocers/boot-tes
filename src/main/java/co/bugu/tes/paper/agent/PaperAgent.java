package co.bugu.tes.paper.agent;

import co.bugu.tes.answer.domain.Answer;
import co.bugu.tes.exam.dto.QuestionDto;
import co.bugu.tes.judge.domain.Judge;
import co.bugu.tes.judge.service.IJudgeService;
import co.bugu.tes.multi.domain.Multi;
import co.bugu.tes.multi.service.IMultiService;
import co.bugu.tes.paper.enums.QuestionTypeEnum;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.service.ISceneService;
import co.bugu.tes.single.domain.Single;
import co.bugu.tes.single.service.ISingleService;
import com.google.common.base.Preconditions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author daocers
 * @Date 2018/11/25:20:42
 * @Description:
 */
@Service
public class PaperAgent {
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
        if (QuestionTypeEnum.SINGLE.getCode() == questionType) {
            Single single = singleService.findById(questionId);
            BeanUtils.copyProperties(single, dto);
//            dto.setRealAnswer(single.getAnswer());
        } else if (QuestionTypeEnum.MULTI.getCode() == questionType) {
            Multi multi = multiService.findById(questionId);
            BeanUtils.copyProperties(multi, dto);
//            dto.setRealAnswer(multi.getAnswer());
        } else if (QuestionTypeEnum.JUDGE.getCode() == questionType) {
            Judge judge = judgeService.findById(questionId);
            BeanUtils.copyProperties(judge, dto);
//            dto.setRealAnswer(judge.getAnswer());
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

        List<Long> sIds = null;
        List<Long> mIds = null;
        List<Long> jIds = null;
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
    private List<Long> getQuestionIdListInSimple(Integer paperGenerateType, Long bankId, Integer questionType, Integer count) throws Exception {
//      TODO  出卷方式待完善   1 随机， 2 统一，3 统一乱序
        List<Long> ids = null;
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
        List<Long> list = ids.subList(0, count);
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
    private List<Long> getQuestionIdListByPolicy(Long paperPolicyId, Integer paperGenerateType, Long bankId, Integer questionType, Integer count) {
//        todo 策略模式待开发
        return null;
    }

}
