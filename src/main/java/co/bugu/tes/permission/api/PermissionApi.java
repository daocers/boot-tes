package co.bugu.tes.permission.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.permission.agent.PermissionAgent;
import co.bugu.tes.permission.domain.Permission;
import co.bugu.tes.permission.dto.PermissionTreeDto;
import co.bugu.tes.permission.service.IPermissionService;
import co.bugu.tes.role.agent.RoleAgent;
import co.bugu.tes.role.domain.Role;
import co.bugu.tes.user.domain.User;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
@RestController
@RequestMapping("/permission/api")
public class PermissionApi {
    private Logger logger = LoggerFactory.getLogger(PermissionApi.class);

    @Autowired
    IPermissionService permissionService;

    @Autowired
    PermissionAgent permissionAgent;

    @Autowired
    RoleAgent roleAgent;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Permission>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Permission permission) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(permission, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Permission> list = permissionService.findByCondition(pageNum, pageSize, permission);
            PageInfo<Permission> pageInfo = new PageInfo<>(list);
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
     * @param permission
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Long> savePermission(@RequestBody Permission permission) {
        try {
            Long userId = UserUtil.getCurrentUser().getId();

            Long permissionId = permission.getId();
            permission.setUpdateUserId(userId);
            if (null == permissionId) {
                permission.setCreateUserId(userId);
                permission.setStatus(BaseStatusEnum.ENABLE.getCode());
                logger.debug("保存， savePermission, 参数： {}", JSON.toJSONString(permission, true));
                permissionId = permissionService.add(permission);
                logger.info("新增 成功， id: {}", permissionId);
            } else {
                permissionService.updateById(permission);
                logger.debug("更新成功", JSON.toJSONString(permission, true));
            }
            return RespDto.success(permissionId);
        } catch (Exception e) {
            logger.error("保存 permission 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.permission.domain.Permission>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Permission> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Permission permission = permissionService.findById(id);
            return RespDto.success(permission);
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
            int count = permissionService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 权限树
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/22 18:04
     */
    @RequestMapping(value = "/getPermissionTree")
    public RespDto<List<PermissionTreeDto>> getPermissionTree() {
        List<PermissionTreeDto> list = permissionAgent.getPermissionTree();
        return RespDto.success(list);
    }

    /**
     * 获取权限列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/30 10:36
     */
    @RequestMapping(value = "/getMenuTree")
    public RespDto<List<PermissionTreeDto>> getMenuTree() throws Exception {
        User user = UserUtil.getCurrentUser();
        List<Role> roles = roleAgent.getRoleList(user.getId());
        if(CollectionUtils.isEmpty(roles)){
            return RespDto.fail("您还没有被分配角色，请联系管理员！");
        }
        boolean isRoot = false;
        for(Role role: roles){
            if("root".equals(role.getCode())){
                isRoot = true;
                break;
            }
        }
        List<PermissionTreeDto> list = null;
        if(isRoot){
            list = permissionAgent.getMenuTree(-1L);
        }else{
            list = permissionAgent.getMenuTree(user.getId());

        }
        return RespDto.success(list);
    }


    /**
     * 查找角色的授权列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/30 15:23
     */
    @RequestMapping(value = "/findPermissionIdList")
    public RespDto<List<Long>> findPermissionIdListByRoleId(Long roleId) {
        List<Long> ids = permissionService.findIdsByRoleId(roleId);
        return RespDto.success(ids);
    }


    @RequestMapping(value = "/saveTree", method = RequestMethod.POST)
    public RespDto<Boolean> saveTree(@RequestBody List<PermissionTreeDto> list) throws Exception {
        Long userId = UserUtil.getCurrentUser().getId();
        permissionService.saveTree(list, userId);
        return RespDto.success(true);
    }

    /**
     * 查找指定用户的url权限
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/4/30 16:23
     */
    @RequestMapping(value = "/findMenuUrlList")
    public RespDto<List<String>> findMenuUrlList() throws UserException {
        Long userId = UserUtil.getCurrentUser().getId();
        List<Role> roleList = roleAgent.getRoleList(userId);

        if(CollectionUtils.isEmpty(roleList)){
            return RespDto.fail("您还没有被分配角色，请联系管理员！");
        }

        boolean isRoot = false;
        for(Role role: roleList){
            if("root".equals(role.getCode())){
                isRoot = true;
                break;
            }
        }
        List<Permission> list = null;
        if(isRoot){
            list = permissionService.findByCondition(new Permission());
        }else{
            list = permissionAgent.findPermissionListByUserId(userId);
        }

        if (CollectionUtils.isEmpty(list)) {
            return RespDto.success(new ArrayList<>());
        }
        List<String> urlList = Lists.transform(list, item -> item.getUrl());
        return RespDto.success(urlList);
    }
}

