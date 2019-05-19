package co.bugu.tes.role.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.role.domain.Role;
import co.bugu.tes.role.dto.RoleDto;
import co.bugu.tes.role.enums.RoleTypeEnum;
import co.bugu.tes.role.service.IRoleService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import co.bugu.tes.userRoleX.domain.UserRoleX;
import co.bugu.tes.userRoleX.service.IUserRoleXService;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
    IUserService userService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<RoleDto>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Role role) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(role, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            if (role != null && StringUtils.isNotEmpty(role.getName())) {
                role.setName("%" + role.getName() + "%");
            }
            PageInfo<Role> pageInfo = roleService.findByConditionWithPage(pageNum, pageSize, role);
            PageInfo<RoleDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            List<RoleDto> list = Lists.transform(pageInfo.getList(), new Function<Role, RoleDto>() {
                @Override
                public RoleDto apply(@Nullable Role role) {
                    RoleDto dto = new RoleDto();
                    BeanUtils.copyProperties(role, dto);
                    User user = userService.findById(role.getCreateUserId());
                    if (user != null) {
                        dto.setCreateUserName(user.getName());
                    }
                    return dto;
                }
            });
            res.setList(list);
            return RespDto.success(res);
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
            Long userId = UserUtil.getCurrentUser().getId();
            Long roleId = role.getId();
            role.setUpdateUserId(userId);
            if (null == roleId) {
                role.setType(RoleTypeEnum.CUSTOM.getType());
                role.setStatus(BaseStatusEnum.ENABLE.getCode());
                role.setCreateUserId(userId);
                logger.debug("保存， saveRole, 参数： {}", JSON.toJSONString(role, true));
                roleId = roleService.add(role);
                logger.info("新增 成功， id: {}", roleId);
            } else {
                Role tmp = roleService.findById(roleId);
                role.setType(tmp.getType());
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
    public RespDto<Boolean> delete(Long id) throws UserException, Exception {
        logger.debug("准备删除， 参数: {}", id);
        Preconditions.checkArgument(id != null, "id不能为空");
        Long userId = UserUtil.getCurrentUser().getId();

        UserRoleX x = new UserRoleX();
        x.setRoleId(id);
        x.setIsDel(DelFlagEnum.NO.getCode());
        PageInfo<UserRoleX> xPageInfo = userRoleXService.findByConditionWithPage(1, 1, x);
        if (xPageInfo.getSize() > 0) {
            throw new Exception("该角色已经分配给用户，不能删除");
        }

        int count = roleService.deleteById(id, userId);

        return RespDto.success(count == 1);
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

    /**
     * 获取我的角色列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/19 11:25
     */
    @RequestMapping(value = "/findMyRoleList")
    public RespDto<List<String>> findRoleListOfCurrentUser() throws UserException {
        Long userId = UserUtil.getCurrentUser().getId();
        UserRoleX query = new UserRoleX();
        query.setUserId(userId);
        query.setIsDel(DelFlagEnum.NO.getCode());
        List<UserRoleX> list = userRoleXService.findByCondition(query);
        if (CollectionUtils.isEmpty(list)) {
            return RespDto.success(new ArrayList<>());
        }
        List<String> roles = Lists.transform(list, item -> {
            Role role = roleService.findById(item.getRoleId());
            return role.getCode();
        });
        return RespDto.success(roles);
    }

}

