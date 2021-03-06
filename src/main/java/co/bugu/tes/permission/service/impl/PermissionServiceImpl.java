package co.bugu.tes.permission.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.permission.dao.PermissionDao;
import co.bugu.tes.permission.domain.Permission;
import co.bugu.tes.permission.dto.PermissionTreeDto;
import co.bugu.tes.permission.service.IPermissionService;
import co.bugu.tes.rolePermissionX.domain.RolePermissionX;
import co.bugu.tes.rolePermissionX.service.IRolePermissionXService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    PermissionDao permissionDao;
    @Autowired
    IRolePermissionXService rolePermissionXService;

    private Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private static String ORDER_BY = "no";


    //    存放权限信息
    Cache<Long, Permission> permissionCache = CacheBuilder.newBuilder()
            .concurrencyLevel(5).maximumSize(200)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .initialCapacity(1)
            .build();

    @Override
    public long add(Permission permission) {
        //todo 校验参数
        permission.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        permission.setCreateTime(now);
        permission.setUpdateTime(now);
        permissionDao.insert(permission);
        return permission.getId();
    }

    @Override
    public int updateById(Permission permission) {
        logger.debug("permission updateById, 参数： {}", JSON.toJSONString(permission, true));
        Preconditions.checkNotNull(permission.getId(), "id不能为空");
        permission.setUpdateTime(new Date());
        return permissionDao.updateById(permission);
    }

    @Override
    public List<Permission> findByCondition(Permission permission) {
        logger.debug("permission findByCondition, 参数： {}", JSON.toJSONString(permission, true));
        PageHelper.orderBy(ORDER_BY);
        List<Permission> permissions = permissionDao.findByObject(permission);

        logger.debug("查询结果， {}", JSON.toJSONString(permissions, true));
        return permissions;
    }

    @Override
    public List<Permission> findByCondition(Integer pageNum, Integer pageSize, Permission permission) {
        logger.debug("permission findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(permission, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Permission> permissions = permissionDao.findByObject(permission);

        logger.debug("查询结果， {}", JSON.toJSONString(permissions, true));
        return permissions;
    }

    @Override
    public PageInfo<Permission> findByConditionWithPage(Integer pageNum, Integer pageSize, Permission permission) {
        logger.debug("permission findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(permission, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Permission> permissions = permissionDao.findByObject(permission);

        logger.debug("查询结果， {}", JSON.toJSONString(permissions, true));
        return new PageInfo<>(permissions);
    }

    @Override
    public Permission findById(Long permissionId) {
        logger.debug("permission findById, 参数 permissionId: {}", permissionId);
        Permission permission = permissionDao.selectById(permissionId);
        logger.debug("查询结果： {}", JSON.toJSONString(permission, true));
        return permission;
    }

    @Override
    public int deleteById(Long permissionId, Long operatorId) {
        logger.debug("permission 删除， 参数 permissionId : {}", permissionId);
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setIsDel(DelFlagEnum.YES.getCode());
        permission.setUpdateTime(new Date());
        permission.setUpdateUserId(operatorId);
        int num = permissionDao.updateById(permission);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

    @Override
    public List<Long> findIdsByRoleId(Long roleId) {
        logger.debug("findIdsByRoleId， roleId: {}", roleId);

        List<Long> res = new ArrayList<>();

        RolePermissionX rolePermissionX = new RolePermissionX();
        rolePermissionX.setRoleId(roleId);
        rolePermissionX.setIsDel(DelFlagEnum.NO.getCode());
        List<RolePermissionX> xList = rolePermissionXService.findByCondition(rolePermissionX);
        if (CollectionUtils.isEmpty(xList)) {
            return res;
        } else {

//            for(RolePermissionX x: xList){
//                Long permissionId = x.getPermissionId();
//                Permission permission = findById(permissionId);
//                if(permission.getType().equals(PermissionTypeEnum.ACTION.getCode())){
//                    res.add(permissionId);
//                }
//            }
            res = Lists.transform(xList, item -> item.getPermissionId());
            return res;
        }

    }

    @Override
    public List<Permission> findByRoleId(Long roleId) {
        List<Long> permissionIds = findIdsByRoleId(roleId);
        List<Permission> res = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(permissionIds)) {
            for (Long id : permissionIds) {
                Permission permission = permissionCache.getIfPresent(id);
                if (null == permission) {
                    permission = permissionDao.selectById(id);
                    if (null == permission) {
                        logger.warn("无效的查询条件，有可能导致缓存穿透，permissionId: {}", id);
                    }
                    permissionCache.put(id, permission);
                }
                res.add(permission);
            }
        }
        return res;
    }

    @Override
    public void saveTree(List<PermissionTreeDto> list, Long userId) {
        saveInRecursion(userId, list);

    }

    private void saveInRecursion(Long userId, List<PermissionTreeDto> dtos) {
        int idx = 1;
        for (PermissionTreeDto dto : dtos) {
            Permission permission = new Permission();
            permission.setId(dto.getId());
            permission.setUpdateUserId(userId);
            permission.setSuperiorId(dto.getSuperiorId());
            permission.setNo(idx);
            permissionDao.updateById(permission);
            idx++;
            if (CollectionUtils.isNotEmpty(dto.getChildren())) {
                saveInRecursion(userId, dto.getChildren());
            }
        }

    }

}
