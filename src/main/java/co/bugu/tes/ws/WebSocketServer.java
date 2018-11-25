package co.bugu.tes.ws;

import co.bugu.tes.answer.service.IAnswerService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
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

    //建立连接后执行
    @OnOpen
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("ConnectionEstablished");
        session.sendMessage(new TextMessage("连接成功"));

        String sessionId = (String) session.getAttributes().get("HTTP.SESSION.ID");
        Long userId = (Long) session.getAttributes().get("userId");
        WebSocketSessionUtil.add(userId, session);

    }

    /**
     * 处理消息
     * @param session
     * @param message
     * @throws Exception
     */
    @OnMessage
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    public void handleMessage(String message, Session session) throws Exception {
        Map<String, Object> res = new HashMap();
        Integer length = message.length();
        logger.info("收到消息：：： {}", message);
        if(StringUtils.isEmpty(message)){
            return;
        }
        JSONObject jsonObject = JSON.parseObject(message);
        Integer type = jsonObject.getInteger("type");
        if(type == MessageEnum.GET_QUESTION.getType()){
            Integer questionId = jsonObject.getInteger("questionId");
//            Question question = questionService.findById(questionId);
//            if(question == null){
//                res.put("code", -1);
//                res.put("msg", "没有查到对应的题目");
//            }else{
//                res.put("code", 0);
//                res.put("title", question.getTitle());
//                res.put("content", question.getContent());
//                res.put("metaInfoId", question.getMetaInfoId());
//                res.put("extraInfo", question.getExtraInfo());
//            }
        }else if(type == MessageEnum.COMMIT_QUESTION.getType()){
//            Integer questionId = jsonObject.getInteger("questionId");
//            String timeLeft = jsonObject.getString("timeLeft");//定时器经过的时间 s
//            String answerInfo = jsonObject.getString("answer");
//            Integer paperId = jsonObject.getInteger("paperId");
//            Answer answer = new Answer();
//            answer.setPaperId(paperId);
//            answer.setQuestionId(questionId);
//            answer.setAnswer(answerInfo);
//            answer.setTimeLeft(timeLeft);
//            answerService.save(answer);

        }else if(type == MessageEnum.COMMIT_PAPER.getType()){
            Integer paperId = jsonObject.getInteger("paperId");
            String answerInfo = jsonObject.getString("answerInfo");
//            if(StringUtils.isEmpty(answerInfo)){
//                Map<String, String> map = JSON.parseObject(answerInfo, Map.class);
//                answerService.savePaperAnswer(map, paperId);
//            }

        }else if(type == MessageEnum.FORSE_COMMIT_PAPER.getType()){
            logger.info("此处消息应该是服务端在考试结束后发往客户端，客户端发起非法");
        }else{
            logger.info("无效消息");
        }
    }

    @OnError
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        logger.error("报错了", exception);
    }

    /**
     * 连接关闭后执行
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @OnClose
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String sessionId = (String) session.getAttributes().get("HTTP.SESSION.ID");
        Long userId = (Long) session.getAttributes().get("userId");
        WebSocketSessionUtil.remove(userId, session);
        logger.debug("afterConnectionClosed" + closeStatus.getReason());

    }

    /**
     * 支持部分信息
     * @return
     */
    public boolean supportsPartialMessages() {
        return false;
    }
}
