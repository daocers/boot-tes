package co.bugu.tes.paper.agnet;

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

import java.util.ArrayList;
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
     * @return
     * @auther daocers
     * @date 2018/11/25 21:06
     */
    public Boolean generatePaper(Long sceneId, Long userId) throws Exception {
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

        List<Long> ids = null;
        if (paperPolicyId > 0) {
//            策略出题
            ids = getQuestionIdListByPolicy(paperPolicyId, paperGenerateType, bankId, singleCount, multiCount, judgeCount);
        } else {
//            简单出题

            ids = getQuestionIdListInSimple(paperGenerateType, bankId, singleCount, multiCount, judgeCount);
        }

        paperService.createPaper(sceneId, userId, ids);

    }

    /**
     * 简单模式出题
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/25 21:36
     */
    private List<Long> getQuestionIdListInSimple(Integer paperGenerateType, Long bankId, Integer singleCount, Integer multiCount, Integer judgeCount) throws Exception {
//      TODO  出卷方式待完善   1 随机， 2 统一，3 统一乱序
        Single single = new Single();
        single.setBankId(bankId);
        Multi multi = new Multi();
        multi.setBankId(bankId);
        Judge judge = new Judge();
        judge.setBankId(bankId);
        List<Long> singleIds = singleService.getAllIds(single);
        List<Long> multiIds = multiService.getAllIds(multi);
        List<Long> judgeIds = judgeService.getAllIds(judge);
        if (singleIds.size() < singleCount) {
            throw new Exception("单选题数量不足");
        }
        if (multiIds.size() < multiCount) {
            throw new Exception("多选题数量不足");
        }

        if (judgeIds.size() < judgeCount) {
            throw new Exception("判断题数量不足");
        }

        List<Long> res = new ArrayList<>();
        Collections.shuffle(singleIds);
        List<Long> sIds = singleIds.subList(0, singleCount);
        Collections.shuffle(multiIds);
        List<Long> mIds = multiIds.subList(0, multiCount);
        Collections.shuffle(judgeIds);
        List<Long> jIds = judgeIds.subList(0, judgeCount);
        res.addAll(sIds);
        res.addAll(mIds);
        res.addAll(jIds);

        return res;

    }


    /**
     * 策略模式出题
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/25 21:37
     */
    private List<Long> getQuestionIdListByPolicy(Long paperPolicyId, Integer paperGenerateType, Long bankId, Integer singleCount, Integer multiCount, Integer judgeCount) {
//        todo 策略模式待开发
        return null;
    }

}
