package co.bugu.util;

import co.bugu.exception.UserException;
import co.bugu.tes.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author daocers
 * @Date 2018/11/19:19:06
 * @Description:
 */
public class TokenUtil {
    private static Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * 为指定用户获取token
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/19 19:09
     */
    public static String getToken(User user) throws Exception {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            String info = user.getId() + user.getPassword() + System.currentTimeMillis();
            md.update(info.getBytes());

            String pwd = new BigInteger(1, md.digest()).toString(16);
            return pwd;
        } catch (NoSuchAlgorithmException e) {
            logger.error("密码加密失败", e);
            throw new UserException("用户数据异常", UserException.TOKEN_ERR);
        }
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
