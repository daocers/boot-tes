package co.bugu.util;

import co.bugu.tes.user.domain.User;

/**
 * @Author daocers
 * @Date 2018/11/19:19:06
 * @Description:
 */
public class TokenUtil {
    /**
     * 为指定用户获取token
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/19 19:09
     */
    public static String getToken(User user) {
        return user.getId() + "";
    }

    /**
     * 通过token获取用户id
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/19 19:09
     */
    public static Long getUserId(String token) {
        return 1L;
    }

    /**
     * 让token失效
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/19 19:09
     */
    public static void invalid(String token) {

    }
}
