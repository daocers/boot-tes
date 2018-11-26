package co.bugu.tes.ws;

import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by daocers on 2017/3/25.
 */
public class WebSocketSessionUtil {
    private static Map<Long, Session> sessionMap = new ConcurrentHashMap<>();

    public static void add(Long userId, Session session) {
        sessionMap.put(userId, session);
    }

    public static void remove(Long userId, WebSocketSession session) {
        sessionMap.remove(userId);
    }

    public static Session getSession(Long userId) {
        return sessionMap.get(userId);
    }

    public static Map<Long, Session> getSessions() {
        return sessionMap;
    }
}
