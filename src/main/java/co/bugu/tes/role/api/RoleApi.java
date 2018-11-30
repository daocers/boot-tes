package co.bugu.tes.role.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.role.domain.Role;
import co.bugu.tes.role.service.IRoleService;
import co.bugu.tes.userRoleX.domain.UserRoleX;
import co.bugu.tes.userRoleX.service.IUserRoleXService;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
@RestController
@RequestMapping("/role/api")
public class RoleApi {
    private Logger logger = LoggerFactory.getLogger(RoleApi.class);

    @Autowired
    IRoleService roleService;
    @Autowired
    IUserRoleXService userRoleXService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Role>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Role role) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(role, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Role> list = roleService.findByCondition(pageNum, pageSize, role);
            PageInfo<Role> pageInfo = new PageInfo<>(list);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(pageInfo);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 查找指定用户的角色id列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/22 20:01
     */
    @RequestMapping(value = "/findByUserId")
    public RespDto<List<Long>> findByUserId(Long userId) {
        UserRoleX query = new UserRoleX();
        query.setUserId(userId);
        query.setIsDel(DelFlagEnum.NO.getCode());
        List<UserRoleX> roles = userRoleXService.findByCondition(query);
        List<Long> ids = Lists.transform(roles, new Function<UserRoleX, Long>() {
            @Override
            public Long apply(@Nullable UserRoleX userRoleX) {
                return userRoleX.getRoleId();
            }
        });
        return RespDto.success(ids);

    }


    /**
     * 保存
     *
     * @param role
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveRole(@RequestBody Role role) {
        try {
            Long roleId = role.getId();
            if (null == roleId) {
                logger.debug("保存， saveRole, 参数： {}", JSON.toJSONString(role, true));
                roleId = roleService.add(role);
                logger.info("新增 成功， id: {}", roleId);
            } else {
                roleService.updateById(role);
                logger.debug("更新成功", JSON.toJSONString(role, true));
            }
            return RespDto.success(roleId != null);
        } catch (Exception e) {
            logger.error("保存 role 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.role.domain.Role>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Role> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Role role = roleService.findById(id);
            return RespDto.success(role);
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
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = roleService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 查找所有的角色
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/30 15:30
     */
    @RequestMapping(value = "/findAll")
    public RespDto<List<Role>> findAll() {
        try {
            List<Role> roles = roleService.findByCondition(null);
            return RespDto.success(roles);
        } catch (Exception e) {
            logger.info("查询全部角色失败", e);
            return RespDto.fail("查询全部角色失败");
        }
    }


    /***
     * 授权  先软删除，再批量添加
     * @Time 2017/12/7 22:51
     * @Author daocers
     * @param   roleId
     * @param  permissionIdList
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     */
    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    public RespDto<Boolean> authorize(@RequestParam Long roleId, @RequestBody List<Long> permissionIdList) {
        try {
            Long userId = UserUtil.getCurrentUser().getId();
            roleService.authorize(roleId, permissionIdList, userId);
            return RespDto.success(true);
        } catch (Exception e) {
            logger.error("授权失败", e);
            return RespDto.fail();
        }

    }

}

