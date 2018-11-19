package co.bugu.tes.user.api;

import co.bugu.common.RespDto;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import co.bugu.util.TokenUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-11-19 17:51
 */
@RestController
@RequestMapping("/user/api")
public class UserApi {
    private Logger logger = LoggerFactory.getLogger(UserApi.class);

    @Autowired
    IUserService userService;



    /**
     * 登录，成功后返回用户token
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/19 17:54
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RespDto<String> login(String username, String password) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotEmpty(username), "用户名不能为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(password), "密码不能为空");

        User user = new User();
        user.setUsername(username);
        List<User> users = userService.findByCondition(user);
        if(CollectionUtils.isNotEmpty(users)){
            if(users.size() > 1){
                logger.warn("用户名相同的用户有两个，数据异常， username: {}", username);
                throw new Exception("数据异常");
            }else{
                user = users.get(0);
            }

            if(!user.getPassword().equals(password)){
                return RespDto.fail(-1, "用户名/密码错误");
            }
//            用户名密码匹配，设置token传给服务端
            String token = TokenUtil.getToken(user);
            return RespDto.success(token);
        }else{
            return RespDto.fail(-1, "用户名/密码错误");
        }

    }

    /**
     * 退出登录，退出成功前端跳回首页
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/19 17:55
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public RespDto<Boolean> logout(Long id, String token){
        Long userId = TokenUtil.getUserId(token);
        if(Objects.equals(userId, id)){
            TokenUtil.invalid(token);
        }else{
            return RespDto.fail(-2, "token无效");
        }
        return RespDto.success(true);
    }


    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<User>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody User user) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(user, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<User> list = userService.findByCondition(pageNum, pageSize, user);
            PageInfo<User> pageInfo = new PageInfo<>(list);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(pageInfo);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 保存
     *
     * @param user
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveUser(@RequestBody User user) {
        try {
            Long userId = user.getId();
            if(null == userId){
                logger.debug("保存， saveUser, 参数： {}", JSON.toJSONString(user, true));
                userId = userService.add(user);
                logger.info("新增 成功， id: {}", userId);
            }else{
                userService.updateById(user);
                logger.debug("更新成功", JSON.toJSONString(user, true));
            }
            return RespDto.success(userId != null);
        } catch (Exception e) {
            logger.error("保存 user 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.user.domain.User>
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/findById")
    public RespDto<User> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            User user = userService.findById(id);
            return RespDto.success(user);
        } catch (Exception e) {
            logger.error("获取详情失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 删除，软删除，更新数据库删除标志
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 17:51
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = userService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

