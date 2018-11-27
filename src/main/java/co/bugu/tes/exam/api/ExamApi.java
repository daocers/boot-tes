package co.bugu.tes.exam.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.answer.domain.Answer;
import co.bugu.tes.answer.service.IAnswerService;
import co.bugu.tes.exam.dto.QuestionDto;
import co.bugu.tes.paper.agnet.PaperAgent;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.enums.PaperStatusEnum;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.service.ISceneService;
import co.bugu.tes.user.domain.User;
import co.bugu.util.UserUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author daocers
 * @Date 2018/11/25:16:36
 * @Description:
 */
@RequestMapping("/exam/api")
@RestController
public class ExamApi {
    private Logger logger = LoggerFactory.getLogger(ExamApi.class);

    @Autowired
    ISceneService sceneService;
    @Autowired
    IPaperService paperService;
    @Autowired
    IAnswerService answerService;

    @Autowired
    PaperAgent paperAgent;

    /**
     * 获取就绪的场次信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/25 16:37
     */
    @RequestMapping(value = "/findReadyScene")
    public RespDto<PageInfo<Scene>> getReadyScene(Integer pageNum, Integer pageSize) {
        User user = UserUtil.getCurrentUser();
        Long userId = user.getId();
        Long departmentId = user.getDepartmentId();
        Scene query = new Scene();
        PageInfo<Scene> pageInfo = sceneService.findByConditionWithPage(pageNum, pageSize, query);
        return RespDto.success(pageInfo);

    }


    /**
     * 检查验证码是否正确
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/25 19:08
     */
    @RequestMapping(value = "/checkAuthCode")
    public RespDto<Boolean> checkAuthCode(String authCode, Long sceneId) {
        Preconditions.checkArgument(sceneId != null);
        Preconditions.checkArgument(StringUtils.isNotEmpty(authCode));
        Scene scene = sceneService.findById(sceneId);

        if (scene != null && scene.getAuthCode().equals(authCode)) {
            return RespDto.success(true);
        } else {
            return RespDto.fail("授权码错误！");
        }
    }


    /**
     * 获取考试列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/25 20:15
     */
    @RequestMapping(value = "/getQuestionList")
    public RespDto<List<QuestionDto>> getQuestionList() {
        User user = UserUtil.getCurrentUser();
        Long userId = user.getId();

        Paper query = new Paper();
        query.setUserId(userId);
        query.setStatus(PaperStatusEnum.OK.getCode());
        query.setIsDel(DelFlagEnum.NO.getCode());
        List<Paper> papers = paperService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(papers) && papers.size() == 1) {
            query = papers.get(0);
            Long paperId = query.getId();
            Answer answer = new Answer();
            answer.setPaperId(paperId);
            answer.setIsDel(DelFlagEnum.NO.getCode());
            List<Answer> answers = answerService.findByCondition(answer);
            List<QuestionDto> dtos = new ArrayList<>();
            for (Answer item : answers) {
                QuestionDto dto = paperAgent.getQuestionDto(item);
                dtos.add(dto);
            }
            return RespDto.success(dtos);
        } else {
            logger.warn("试卷信息有误，用户ID：", userId);
            return RespDto.fail("获取试卷信息失败");
        }

    }


    @RequestMapping(value = "/generatePaper", method = RequestMethod.POST)
    public RespDto<Boolean> generatePaper(Long sceneId){
        User user = UserUtil.getCurrentUser();
        Long userId = user.getId();

        Boolean res = paperAgent.generatePaper(sceneId, userId);
        if(res){
            return RespDto.success(true);
        }else{
            logger.warn("生成试卷失败");
            return RespDto.fail("生成试卷失败");
        }
    }
}