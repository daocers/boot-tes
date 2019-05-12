package co.bugu.tes.ws;

import co.bugu.common.RespDto;
import co.bugu.tes.answer.service.IAnswerService;
import co.bugu.tes.exam.dto.QuestionDto;
import co.bugu.tes.paper.agent.PaperAgent;
import co.bugu.util.ApplicationContextUtil;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author daocers
 * @Date 2018/11/25:19:26
 * @Description:
 */
@ServerEndpoint(value = "/ws/hn.ws")
@Component
public class WebSocketServer {
    private Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @Autowired
    IAnswerService answerService;
    @Autowired
    PaperAgent paperAgent;

    //建立连接后执行
    @OnOpen
    public void afterConnectionEstablished(Session session) throws Exception {
        answerService = ApplicationContextUtil.getClass(IAnswerService.class);
        paperAgent = ApplicationContextUtil.getClass(PaperAgent.class);

        logger.debug("连接成功");

//        List<String> userIdInfo = session.getRequestParameterMap().get("userId");
        List<String> sceneInfo = session.getRequestParameterMap().get("sceneId");
        List<String> tokenInfo = session.getRequestParameterMap().get("token");
//        Long userId = Long.parseLong(userIdInfo.get(0));
        Long sceneId = Long.parseLong(sceneInfo.get(0));
        String token = tokenInfo.get(0);
        Long userId = UserUtil.getUserIdByToken(token);

//        token和userId匹配
        if (null != userId && userId > 0) {

            session.getBasicRemote().sendText(JSON.toJSONString(RespDto.success()));

            //      存入session
            WebSocketSessionUtil.add(userId, session);

            WebSocketSessionUtil.addOnlineUser(userId, session);
            WebSocketSessionUtil.addSceneUser(sceneId, userId);

            return;
        } else {
//            token 和userId不匹配
            session.getBasicRemote().sendText(JSON.toJSONString(RespDto.fail(-1, "非法用户信息，请联系管理员")));
            session.close();
        }


    }

    /**
     * 处理消息
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @OnMessage
    public void handleMessage(String message, Session session) throws Exception {
        Map<String, String> res = new HashMap<>();
        res.put("code", "0");
        res.put("message", "ok");
        logger.info("收到消息：：： {}", message);
        if (StringUtils.isEmpty(message)) {
            return;
        }

//        List<String> userIdInfo = session.getRequestParameterMap().get("userId");
//        Long userId = Long.parseLong(userIdInfo.get(0));
        List<String> sceneInfo = session.getRequestParameterMap().get("sceneId");
        Long sceneId = Long.parseLong(sceneInfo.get(0));
        List<String> tokenInfo = session.getRequestParameterMap().get("token");
        String token = tokenInfo.get(0);
        Long userId = UserUtil.getUserIdByToken(token);


        JSONObject jsonObject = JSON.parseObject(message);
        Integer type = jsonObject.getInteger("type");
        if (type == MessageTypeEnum.GET_QUESTION.getCode()) {
//            获取题目
            logger.info("获取试题信息");
        } else if (type == MessageTypeEnum.COMMIT_QUESTION.getCode()) {
//            提交题目
            logger.info("提交试题");
            QuestionDto dto = jsonObject.getObject("content", QuestionDto.class);
            logger.debug("提交的试题信息", JSON.toJSONString(dto, true));
            dto.setUserId(userId);
            int num = paperAgent.commitQuestion(dto);
            res.put("count", num + "");

        } else if (type == MessageTypeEnum.COMMIT_PAPER.getCode()) {
//            提交试卷暂时不从websocket处理

        } else if (type == MessageTypeEnum.FORCE_COMMIT_PAPER.getCode()) {
            logger.info("强制提交试卷");
            logger.info("此处消息应该是服务端在考试结束后发往客户端，客户端发起非法");
        } else if (type == MessageTypeEnum.CLIENT_CLOSE.getCode()) {

//            清空在内存中的引用
            WebSocketSessionUtil.remove(userId);


            WebSocketSessionUtil.removeOnlineUser(userId);
            WebSocketSessionUtil.removeSceneUser(sceneId, userId);
            session.close(new CloseReason(CloseReason.CloseCodes.NO_STATUS_CODE, "客户端发起"));
            return;
        } else {
            logger.warn("无效消息, {}", message);
        }
//        发送响应消息
        session.getBasicRemote().sendText(JSON.toJSONString(res));
    }

    @OnError
    public void handleTransportError(Session session, Throwable exception) throws Exception {
        logger.error("ws异常");
        if (session.isOpen()) {
            session.close();
        }
        logger.error("报错了", exception);
    }

    /**
     * 连接关闭后执行
     *
     * @throws Exception
     */
    @OnClose
    public void afterConnectionClosed() {
        logger.info("连接关闭");
    }

    /**
     * 支持部分信息
     *
     * @return
     */
    public boolean supportsPartialMessages() {
        return false;
    }
}
