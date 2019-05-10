package co.bugu.util;

import co.bugu.exception.UserException;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author daocers
 * @Date 2018/11/21:15:08
 * @Description:
 */
public class UserUtil {

    private static Logger logger = LoggerFactory.getLogger(UserUtil.class);
    //    最大1000人，最后访问30分钟之后失效
    private static Cache<String, Long> userTokenCache = CacheBuilder.newBuilder()
            .concurrencyLevel(3)
            .maximumSize(1000)  //控制多少人登录，用来区分不同版本的使用人数
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .initialCapacity(100)
            .build();

    static IUserService userService;


    /**
     * 校验token和userId是否匹配
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/10 11:09
     */
    public static Boolean checkToken(Long userId, String token) {
        Long id = userTokenCache.getIfPresent(token);
        if (id == null) {
//            token无效
            logger.warn("token： {}无效", token);
            return false;
        }
        if (Objects.equals(id, userId)) {
            return true;
        } else {
            logger.warn("userId：{} 和token： {}不匹配", new Object[]{userId, token});
            return false;
        }

    }

    /**
     * 校验token是否合法
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/10 11:05
     */
    public static Boolean checkToken(String token) {
        Long userId = userTokenCache.getIfPresent(token);
        if (null == userId) {
            return false;
        }
        return true;
    }

    /**
     * 获取当前登录用户
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/21 15:09
     */
    public static User getCurrentUser() throws UserException {
        String token = ThreadLocalUtil.getUserToken();
        if (StringUtils.isEmpty(token)) {
            throw new UserException("用户token不存在", UserException.TOKEN_ERR);
        }
        Long userId = userTokenCache.getIfPresent(token);
        if (null == userId) {
            throw new UserException("登录已过期，请重新登录", UserException.TOKEN_ERR);
        }
        if (null == userService) {
            userService = ApplicationContextUtil.getClass(IUserService.class);
        }
        User user = userService.findById(userId);
        return user;
    }


    /**
     * 保存用户token
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/3 10:41
     */
    public static void saveUserToken(Long userId, String token) {
        userTokenCache.put(token, userId);
    }


    /**
     * 清除指定的token
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/3 15:52
     */
    public static void invalidToken() {
        String token = ThreadLocalUtil.getUserToken();
        userTokenCache.invalidate(token);

    }

    /**
     * 获取token的数量
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/4 21:57
     */
    public static Long getTokenCount() {
        return userTokenCache.size();
    }


    /**
     * 通过token获取用户id
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/10 11:53
     */
    public static Long getUserIdByToken(String token) {
        Long userId = userTokenCache.getIfPresent(token);
        return userId;
    }
}
