package co.bugu.util;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.permission.domain.Permission;
import co.bugu.tes.permission.service.IPermissionService;
import co.bugu.tes.rolePermissionX.domain.RolePermissionX;
import co.bugu.tes.rolePermissionX.service.IRolePermissionXService;
import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存辅助类
 *
 * @Author daocers
 * @Date 2018/11/30:11:00
 * @Description:
 */
public class CacheUtil {
    static IPermissionService permissionService;
    static IRolePermissionXService rolePermissionXService;
//    static IBranchService branchService;
//    static IDepartmentService departmentService;
//    static IStationService stationService;



    private static Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    static {
        permissionService = ApplicationContextUtil.getClass(IPermissionService.class);
        rolePermissionXService = ApplicationContextUtil.getClass(IRolePermissionXService.class);
//        branchService = ApplicationContextUtil.getClass(IBranchService.class);
//        departmentService = ApplicationContextUtil.getClass(IDepartmentService.class);
//        stationService = ApplicationContextUtil.getClass(IStationService.class);
    }

    //    角色对应的权限列表
    static Cache<Long, List<Long>> rolePerIdsCache = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES)
            .concurrencyLevel(3).build();

    /**
     * 缓存权限信息
     */
    static Cache<Long, Permission> permissionCache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES)
            .concurrencyLevel(3).maximumSize(500).build();

    public static List<Long> getPermissionIdsByRoleId(Long roleId) {
        List<Long> res = rolePerIdsCache.getIfPresent(roleId);
        if (res != null) {
            return res;
        }

        RolePermissionX query = new RolePermissionX();
        query.setIsDel(DelFlagEnum.NO.getCode());
        query.setRoleId(roleId);
        List<RolePermissionX> xList = rolePermissionXService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(xList)) {
            res = Lists.transform(xList, new Function<RolePermissionX, Long>() {
                @Override
                public Long apply(@Nullable RolePermissionX rolePermissionX) {
                    return rolePermissionX.getPermissionId();
                }
            });
        } else {
            res = new ArrayList<>();
        }
        rolePerIdsCache.put(roleId, res);
        return res;
    }

    public static Permission getPermission(Long permissionId) {
        Permission permission = permissionCache.getIfPresent(permissionId);
        if (null != permission) {
            return permission;
        }
        permission = permissionService.findById(permissionId);
        if (permission == null) {
//            防止缓存穿透
            permission = new Permission();
        }
        permissionCache.put(permissionId, permission);
        return permission;

    }

}
