package co.bugu.tes.permission.agent;

import co.bugu.tes.permission.domain.Permission;
import co.bugu.tes.permission.dto.PermissionTreeDto;
import co.bugu.tes.permission.service.IPermissionService;
import co.bugu.tes.rolePermissionX.service.IRolePermissionXService;
import co.bugu.tes.user.service.IUserService;
import co.bugu.tes.userRoleX.domain.UserRoleX;
import co.bugu.tes.userRoleX.service.IUserRoleXService;
import co.bugu.util.CacheHolderUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 权限业务逻辑类
 *
 * @author daocers
 * @createTime 2017/12/5
 */
@Service
public class PermissionAgent {
    private static Logger logger = LoggerFactory.getLogger(PermissionAgent.class);

    @Autowired
    IPermissionService permissionService;
    @Autowired
    IUserService userService;
    @Autowired
    IRolePermissionXService rolePermissionXService;
    @Autowired
    IUserRoleXService userRoleXService;

    public List<PermissionTreeDto> getPermissionTree() {
        List<Permission> permissionList = permissionService.findByCondition(null);
        if (CollectionUtils.isEmpty(permissionList)) {
            return new ArrayList<>();
        }
        List<PermissionTreeDto> res = transformListToTree(permissionList);
        return res;
    }

    /**
     * 将平铺的列表展示成树
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/30 11:28
     */
    private List<PermissionTreeDto> transformListToTree(List<Permission> list) {
//        List<PermissionTreeDto> treeDtos = Lists.transform(list, new Function<Permission, PermissionTreeDto>() {
//            @Nullable
//            @Override
//            public PermissionTreeDto apply(@Nullable Permission permission) {
//                PermissionTreeDto dto = new PermissionTreeDto();
//                BeanUtils.copyProperties(permission, dto);
//                return dto;
//            }
//        });

        List<PermissionTreeDto> treeDtos = new ArrayList<>();
        for (Permission permission : list) {
            PermissionTreeDto dto = new PermissionTreeDto();
            BeanUtils.copyProperties(permission, dto);
            treeDtos.add(dto);
        }

        Map<Long, List<PermissionTreeDto>> info = new HashMap<>();
        List<PermissionTreeDto> rootList = new ArrayList<>();
        for (PermissionTreeDto dto : treeDtos) {
            Long id = dto.getSuperiorId();
            if (!info.containsKey(id)) {
                info.put(id, new ArrayList<>());
            }
            info.get(id).add(dto);
            if (null == id || id < 1) {
                rootList.add(dto);
            }
        }

        for (PermissionTreeDto dto : rootList) {
            dto.setChildren(getChildren(dto.getId(), info));
        }
        return rootList;
    }


    private List<PermissionTreeDto> getChildren(Long id, Map<Long, List<PermissionTreeDto>> info) {
        List<PermissionTreeDto> children = info.get(id);
        if (CollectionUtils.isNotEmpty(children)) {
            for (PermissionTreeDto dto : children) {
                dto.setChildren(getChildren(dto.getId(), info));
            }
        }
        return children;
    }

    /**
     * 获取菜单列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/30 10:37
     */
    public List<PermissionTreeDto> getMenuTree(Long userId) {
        List<Long> permissionIds = findPermissionIdsByUserId(userId);
        List<Permission> permissionList = Lists.transform(permissionIds, new Function<Long, Permission>() {
            @Override
            public Permission apply(@org.checkerframework.checker.nullness.qual.Nullable Long permissionId) {
                Permission permission = permissionService.findById(permissionId);
                return permission;
            }
        });
        List<PermissionTreeDto> res = transformListToTree(permissionList);
        return res;
    }


    /**
     * 查找指定用户的权限id列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/30 10:54
     */
    public List<Long> findPermissionIdsByUserId(Long userId) {
        List<Long> roleIds = CacheHolderUtil.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            UserRoleX x = new UserRoleX();
            x.setUserId(userId);
            List<UserRoleX> xList = userRoleXService.findByCondition(x);
            if (CollectionUtils.isEmpty(xList)) {
                return new ArrayList<>();
            }
            roleIds = Lists.transform(xList, userRoleX -> userRoleX.getRoleId());
            CacheHolderUtil.putUserRoleList(userId, roleIds);

        }
        List<Long> res = new ArrayList<>();
        for (Long roleId : roleIds) {
            List<Long> pids = CacheHolderUtil.getPermissionIdsByRoleId(roleId);
            if (CollectionUtils.isEmpty(pids)) {
                List<Long> permissionIds = permissionService.findIdsByRoleId(roleId);
                if (CollectionUtils.isNotEmpty(permissionIds)) {
                    res.addAll(permissionIds);
                    CacheHolderUtil.putRolePermissionList(roleId, permissionIds);
                }
            }
        }
        return res;

    }

    public List<Permission> findPermissionListByUserId(Long userId) {
        List<Long> pIds = findPermissionIdsByUserId(userId);
        if (CollectionUtils.isEmpty(pIds)) {
            return null;
        }

        List<Permission> res = new CopyOnWriteArrayList<>();

        pIds.parallelStream().forEach(permissionId -> {
            Permission permission = CacheHolderUtil.getPermission(permissionId);
            if (null == permission) {
                permission = permissionService.findById(permissionId);
            }
            if (permission != null) {
                CacheHolderUtil.putPermission(permissionId, permission);
                res.add(permission);
            }
        });
        return res;
    }


    /**
     * 检查用户是否有权限访问某个url
     *
     * @param userId
     * @param url
     * @return
     */
    public boolean checkAuthForUser(Long userId, String url) {
        List<Long> pIds = findPermissionIdsByUserId(userId);
        if (CollectionUtils.isEmpty(pIds)) {
            return false;
        }
        for (Long pId : pIds) {
            Permission permission = CacheHolderUtil.getPermission(pId);
            if (null == permission) {
                permission = permissionService.findById(pId);
            }
            if (null != permission) {
                String pUrl = permission.getUrl();
                if (url.endsWith(pUrl)) {
                    return true;
                }
            }

        }
        return false;
    }


}
