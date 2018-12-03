package co.bugu.tes.exam.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.answer.domain.Answer;
import co.bugu.tes.answer.service.IAnswerService;
import co.bugu.tes.exam.dto.LiveDto;
import co.bugu.tes.exam.dto.QuestionDto;
import co.bugu.tes.paper.agent.PaperAgent;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.enums.PaperStatusEnum;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.enums.SceneStatusEnum;
import co.bugu.tes.scene.service.ISceneService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import co.bugu.tes.ws.MessageTypeEnum;
import co.bugu.tes.ws.WebSocketSessionUtil;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import java.util.*;

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
    @Autowired
    IUserService userService;

    /**
     * 获取就绪的场次信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/25 16:37
     */
    @RequestMapping(value = "/findReadyScene")
    public RespDto<PageInfo<Scene>> getReadyScene(Integer pageNum, Integer pageSize) throws UserException {
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
    public RespDto<List<QuestionDto>> getQuestionList(Long sceneId) throws UserException {
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


    /**
     * 生成试卷
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/26 11:17
     */
    @RequestMapping(value = "/getPaper", method = RequestMethod.POST)
    public RespDto<Long> getPaper(Long sceneId, String authCode) {
        try {
            User user = UserUtil.getCurrentUser();
            Long userId = user.getId();

            Paper query = new Paper();
            query.setIsDel(DelFlagEnum.NO.getCode());
            query.setSceneId(sceneId);
            query.setUserId(userId);
            query.setStatus(PaperStatusEnum.OK.getCode());
            List<Paper> list = paperService.findByCondition(query);
            if (CollectionUtils.isNotEmpty(list)) {
                logger.info("已经生成过试卷了，直接进入考试");
                return RespDto.success(list.get(0).getId());
            }

            Long paperId = paperAgent.generatePaper(sceneId, userId);
            return RespDto.success(paperId);
        } catch (Exception e) {
            logger.error("生成试卷失败", e);
            return RespDto.fail("生成试卷失败");
        }


    }


    /**
     * 获取本场考试还剩下多少时间，单位毫秒
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/26 14:55
     */
    @RequestMapping(value = "/getTimeLeft")
    public RespDto<Long> getTimeLeft(Long sceneId) throws UserException {
        User user = UserUtil.getCurrentUser();
        Long userId = user.getId();
        Paper query = new Paper();
        query.setStatus(PaperStatusEnum.OK.getCode());
        query.setIsDel(DelFlagEnum.NO.getCode());
        query.setSceneId(sceneId);
        query.setUserId(userId);


//        todo  判断剩余时间，让考试可以继续进行
        List<Paper> papers = paperService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(papers)) {
            Long paperId = papers.get(0).getId();
            Answer answer = new Answer();
            answer.setPaperId(paperId);
            List<Answer> answers = answerService.findByCondition(1, 10, answer);
            answer = answers.get(0);
            String info = answer.getTimeLeft();
//            long res = info.substring(0,2) * 3600 + info.substring(3,5)
        } else {
//            理论上不可达
        }
        return null;
    }


    /**
     * 提交试卷
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/27 15:06
     */
    @RequestMapping(value = "/commitPaper", method = RequestMethod.POST)
    public RespDto<Boolean> commitPaper(Long paperId, @RequestBody List<QuestionDto> questionDtos) throws Exception {
        logger.info("提交试卷。。。");
        logger.debug("试卷id: {}", paperId);
        logger.debug("答案信息： {}", JSON.toJSONString(questionDtos, true));

        paperAgent.commitPaper(paperId, questionDtos);
        return RespDto.success(true);
    }


    /**
     * 查看指定场次的考试信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/27 16:29
     */
    @RequestMapping(value = "/live")
    public RespDto<PageInfo<LiveDto>> getSceneLive(Long sceneId, Integer pageNum, Integer pageSize) {
        Paper paper = new Paper();
        paper.setSceneId(sceneId);
        paper.setIsDel(DelFlagEnum.NO.getCode());
        PageInfo<Paper> pageInfo = paperService.findByConditionWithPage(pageNum, pageSize, paper);

        PageInfo<LiveDto> res = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, res);
        List<LiveDto> dtos = Lists.transform(pageInfo.getList(), new Function<Paper, LiveDto>() {
            @Override
            public LiveDto apply(@Nullable Paper paper) {
                LiveDto dto = new LiveDto();

                Long userId = paper.getUserId();
                User user = userService.findById(userId);
                dto.setUserId(userId);
                dto.setName(user.getName());
                Session session = WebSocketSessionUtil.getSession(userId);
                if (session != null) {
                    dto.setUserStatus(1);
                } else {
                    dto.setUserStatus(2);
                }

                dto.setPaperStatus(paper.getStatus());
                dto.setPaperId(paper.getId());
                return dto;
            }
        });
        res.setList(dtos);
        return RespDto.success(res);
    }

    /**
     * 强制调教指定用户的试卷
     * 调用之后给客户端通过websocket发消息，
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/27 16:59
     */
    @RequestMapping(value = "/forceCommit", method = RequestMethod.POST)
    public RespDto<Boolean> forceCommit(Long sceneId, Long userId) {
        try {
            Session session = WebSocketSessionUtil.getSession(userId);
            if (session != null) {
                Map<String, String> res = new HashMap<>();
                res.put("type", MessageTypeEnum.FORCE_COMMIT_PAPER.getCode() + "");
                res.put("content", "管理员强制封场，交卷");
                session.getBasicRemote().sendText(JSON.toJSONString(res));
                return RespDto.success(true);
            } else {
                logger.info("该用户已经离场");
                return RespDto.success(true);
            }
        } catch (Exception e) {
            logger.error("强制交卷失败", e);
            return RespDto.fail("强制交卷失败");
        }

    }

    /**
     * 是否可以进行本场考试
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/29 22:38
     */
    @RequestMapping(value = "/canAccess")
    public RespDto<Boolean> canAccess(Long sceneId, String authCode) {
        Date now = new Date();
        Scene scene = sceneService.findById(sceneId);
        if (!scene.getAuthCode().equals(authCode)) {
            return RespDto.fail(0, "授权码错误");
        }
        if (scene.getStatus() == SceneStatusEnum.READY.getCode()) {
            return RespDto.fail(-1, "本场考试未开始");
        }
        if (scene.getStatus() == SceneStatusEnum.CLOSED.getCode()) {
            return RespDto.fail(-1, "本场考试已结束");
        }
        if (scene.getStatus() == SceneStatusEnum.CANCELED.getCode()) {
            return RespDto.fail(-1, "场次已作废");
        }
        if (scene.getOpenTime().after(now)) {
            return RespDto.fail(-1, "考试未开始");
        }
        if (scene.getOpenTime().getTime() + scene.getDuration() * 60000 < now.getTime()) {
            return RespDto.fail(-1, "迟到太久，参加下一场吧");
        }

        return RespDto.success();

    }
}
