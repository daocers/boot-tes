package co.bugu.util;

/**
 * @Author daocers
 * @Date 2018/12/3:10:15
 * @Description:
 */
public class ThreadLocalUtil {
    private static ThreadLocal<String> userToken = new ThreadLocal<>();

    private static ThreadLocal<Long> userIdLocal = new ThreadLocal<>();


    /**
     * 设置当前用户token
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/3 10:17
     */
    public static void setUserToken(String token) {
        userToken.set(token);
    }

    /**
     * 获取当前用户的token
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/3 10:17
     */
    public static String getUserToken() {
        return userToken.get();
    }


    public static Long getUserId() {
        return userIdLocal.get();
    }

    public static void setUserId(Long userId) {
        userIdLocal.set(userId);
    }
}
