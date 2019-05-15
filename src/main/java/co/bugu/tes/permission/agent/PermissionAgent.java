package co.bugu.tes.permission.agent;

import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.permission.domain.Permission;
import co.bugu.tes.permission.dto.PermissionTreeDto;
import co.bugu.tes.permission.service.IPermissionService;
import co.bugu.tes.rolePermissionX.domain.RolePermissionX;
import co.bugu.tes.rolePermissionX.service.IRolePermissionXService;
import co.bugu.tes.user.service.IUserService;
import co.bugu.tes.userRoleX.domain.UserRoleX;
import co.bugu.tes.userRoleX.service.IUserRoleXService;
import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

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

    /**
     * 指定id的角色和对应的权限id列表的缓存
     *
     * @param
     * @return
     * @author daocers
     * @data 2019/4/4 15:11
     */
    Cache<Long, List<Long>> roleIdPermIdsCache = CacheBuilder.newBuilder().concurrencyLevel(4)
            .maximumSize(100)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    /**
     * 权限的缓存
     *
     * @param
     * @return
     * @author daocers
     * @data 2019/4/4 15:13
     */
    Cache<Long, Permission> permissionCache = CacheBuilder.newBuilder().concurrencyLevel(8)
            .maximumSize(1000)
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .build();


    /**
     * 权限的url和对应的id的缓存
     *
     * @param
     * @return
     * @author daocers
     * @data 2019/4/4 15:27
     */
    Cache<String, Long> urlPermissionIdCache = CacheBuilder.newBuilder().concurrencyLevel(3)
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

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
            if(permission == null){
                continue;
            }
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
        List<Permission> permissionList = null;
        if(userId == null || userId < 0){
            Permission query = new Permission();
            query.setIsDel(DelFlagEnum.NO.getCode());
            query.setStatus(BaseStatusEnum.ENABLE.getCode());
            permissionList = permissionService.findByCondition(query);
        }else{
            List<Long> permissionIds = findPermissionIdsByUserId(userId);
            permissionList = Lists.transform(permissionIds, new Function<Long, Permission>() {
                @Override
                public Permission apply(@org.checkerframework.checker.nullness.qual.Nullable Long permissionId) {
                    Permission permission = permissionService.findById(permissionId);
                    return permission;
                }
            });
        }

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
        List<Long> res = new ArrayList<>();


        UserRoleX query = new UserRoleX();
        query.setUserId(userId);
        List<UserRoleX> xList = userRoleXService.findByCondition(query);
        if (CollectionUtils.isEmpty(xList)) {
            return res;
        }

        Collection<Long> set = new HashSet<>();
        for (UserRoleX x : xList) {
            Long roleId = x.getRoleId();
            List<Long> pIds = roleIdPermIdsCache.getIfPresent(roleId);
            if (CollectionUtils.isEmpty(pIds)) {
                RolePermissionX rolePermissionX = new RolePermissionX();
                rolePermissionX.setRoleId(roleId);
                List<RolePermissionX> rolePermissionXList = rolePermissionXService.findByCondition(rolePermissionX);
                if (CollectionUtils.isEmpty(rolePermissionXList)) {
//                    防止缓存穿透
                    roleIdPermIdsCache.put(roleId, new ArrayList<>());
                } else {
                    pIds = Lists.transform(rolePermissionXList, item -> item.getPermissionId());
                    roleIdPermIdsCache.put(roleId, pIds);
//
                }

            }

//            添加到结果中
            set.addAll(pIds);
        }
        res.addAll(set);
        return res;

    }


    /**
     * 查找指定用户的所有权限信息
     *
     * @param
     * @return
     * @author daocers
     * @data 2019/4/4 15:20
     */
    public List<Permission> findPermissionListByUserId(Long userId) {
//        先找用户的权限id列表
        List<Long> pIds = findPermissionIdsByUserId(userId);
        if (CollectionUtils.isEmpty(pIds)) {
            return new ArrayList<>();
        }

        List<Permission> res = new CopyOnWriteArrayList<>();

        pIds.stream().forEach(permissionId -> {
            Permission permission = permissionCache.getIfPresent(permissionId);
            if (null == permission) {
                permission = permissionService.findById(permissionId);
            }
            if (null != permission) {
                res.add(permission);
                permissionCache.put(permissionId, permission);

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
        if (StringUtils.isEmpty(url)) {
            logger.warn("权限url为空");
            return true;
        }
        Long pId = urlPermissionIdCache.getIfPresent(url);
        if (pId == null) {
            Permission query = new Permission();
            query.setUrl(url);
            List<Permission> list = permissionService.findByCondition(query);
            if (CollectionUtils.isEmpty(list)) {
                logger.warn("系统中没有包含指定的url：{}", url);
                return false;
            }
            pId = list.get(0).getId();
            urlPermissionIdCache.put(url, pId);
        }
        List<Long> pIds = findPermissionIdsByUserId(userId);

        if (pIds.contains(pId)) {
            return true;
        }
        return false;
    }


}
