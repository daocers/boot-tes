package co.bugu.util;

import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;

/**
 * @Author daocers
 * @Date 2018/11/21:15:08
 * @Description:
 */
public class UserUtil {

    static IUserService userService;

    /**
     * 获取当前登录用户
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/21 15:09
     */
    public static User getCurrentUser(){
//        todo  拦截token获取用户id
        if(null == userService){
            userService = ApplicationContextUtil.getClass(IUserService.class);
        }

        User user = userService.findById(1L);
        return user;
    }
}
