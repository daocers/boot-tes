package co.bugu.tes.permission.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.permission.dao.PermissionDao;
import co.bugu.tes.permission.domain.Permission;
import co.bugu.tes.permission.service.IPermissionService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-11-19 17:52
 */
@Service
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    PermissionDao permissionDao;

    private Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

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
        permission.setUpdatePermissionId(operatorId);
        int num = permissionDao.updateById(permission);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
