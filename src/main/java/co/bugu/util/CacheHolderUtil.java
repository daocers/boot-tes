package co.bugu.util;

import co.bugu.tes.permission.domain.Permission;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 保存相关的缓存信息
 */
public class CacheHolderUtil {
    static Cache<Long, Permission> permissionCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(1000)
            .concurrencyLevel(10)
            .build();

    static Cache<Long, List<Long>> roleIdPermissionIdsCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .maximumSize(1000)
            .concurrencyLevel(10)
            .build();


    static Cache<Long, List<Long>> userIdRolesCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .maximumSize(1000)
            .concurrencyLevel(10)
            .build();

    public static void putPermission(Long permissionId, Permission permission) {
        permissionCache.put(permissionId, permission);
    }

    public static Permission getPermission(Long permissionId) {
        return permissionCache.getIfPresent(permissionId);
    }


    public static void putRolePermissionList(Long roleId, List<Long> permissionIds) {
        roleIdPermissionIdsCache.put(roleId, permissionIds);
    }


    public static List<Long> getPermissionIdsByRoleId(Long roleId) {
        return roleIdPermissionIdsCache.getIfPresent(roleId);
    }

    public static void  putUserRoleList(Long userId, List<Long> roleIds){
        userIdRolesCache.put(userId, roleIds);
    }

    public static List<Long> getRoleIdListByUserId(Long userId){
        return userIdRolesCache.getIfPresent(userId);
    }


}
