package co.bugu.tes.ws;

import org.apache.commons.collections4.CollectionUtils;

import javax.websocket.Session;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by daocers on 2017/3/25.
 */
public class WebSocketSessionUtil {
    //    使用concurrentHashMap, 避免多线程下的问题
    private static Map<Long, Session> sessionMap = new ConcurrentHashMap<>();

    //    保存在线用户信息
    private static Map<Long, Session> userMap = new ConcurrentHashMap<>();
    //    指定场次的用户信息
    private static Map<Long, Set<Long>> sceneMap = new ConcurrentHashMap<>();


    public static void addOnlineUser(Long userId, Session session) {
        userMap.put(userId, session);
    }

    public static void removeOnlineUser(Long userId){
        sessionMap.remove(userId);
    }

    public static void addSceneUser(Long sceneId, Long userId){
        if(CollectionUtils.isEmpty(sceneMap.get(sceneId))){
            sceneMap.put(sceneId, new HashSet<>());
        }

//        防止多线程问题
        synchronized (WebSocketSessionUtil.class){
            sceneMap.get(sceneId).add(userId);
        }
    }

    public static boolean removeSceneUser(Long sceneId, Long userId){
        Set<Long> set = sceneMap.get(sceneId);
//        非空才执行，否则返回false
        if(CollectionUtils.isNotEmpty(set)){
            return set.remove(userId);
        }

        return false;
    }

    public static void add(Long userId, Session session) {
        sessionMap.put(userId, session);
    }

    public static void remove(Long userId) {
        sessionMap.remove(userId);
    }

    public static Session getSession(Long userId) {
        return sessionMap.get(userId);
    }

    public static Map<Long, Session> getSessions() {
        return sessionMap;
    }
}
