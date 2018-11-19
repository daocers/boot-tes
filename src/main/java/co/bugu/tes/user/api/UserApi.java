package co.bugu.tes.user.api;

import co.bugu.common.RespDto;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

