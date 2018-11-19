package co.bugu.tes.rolePermission.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.rolePermission.dao.RolePermissionDao;
import co.bugu.tes.rolePermission.domain.RolePermission;
import co.bugu.tes.rolePermission.service.IRolePermissionService;
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
 * @create 2018-11-19 17:51
 */
@Service
public class RolePermissionServiceImpl implements IRolePermissionService {
    @Autowired
    RolePermissionDao rolePermissionDao;

    private Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(RolePermission rolePermission) {
        //todo 校验参数
        rolePermission.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        rolePermission.setCreateTime(now);
        rolePermission.setUpdateTime(now);
        rolePermissionDao.insert(rolePermission);
        return rolePermission.getId();
    }

    @Override
    public int updateById(RolePermission rolePermission) {
        logger.debug("rolePermission updateById, 参数： {}", JSON.toJSONString(rolePermission, true));
        Preconditions.checkNotNull(rolePermission.getId(), "id不能为空");
        rolePermission.setUpdateTime(new Date());
        return rolePermissionDao.updateById(rolePermission);
    }

    @Override
    public List<RolePermission> findByCondition(RolePermission rolePermission) {
        logger.debug("rolePermission findByCondition, 参数： {}", JSON.toJSONString(rolePermission, true));
        PageHelper.orderBy(ORDER_BY);
        List<RolePermission> rolePermissions = rolePermissionDao.findByObject(rolePermission);

        logger.debug("查询结果， {}", JSON.toJSONString(rolePermissions, true));
        return rolePermissions;
    }

    @Override
    public List<RolePermission> findByCondition(Integer pageNum, Integer pageSize, RolePermission rolePermission) {
        logger.debug("rolePermission findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(rolePermission, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<RolePermission> rolePermissions = rolePermissionDao.findByObject(rolePermission);

        logger.debug("查询结果， {}", JSON.toJSONString(rolePermissions, true));
        return rolePermissions;
    }

    @Override
    public PageInfo<RolePermission> findByConditionWithPage(Integer pageNum, Integer pageSize, RolePermission rolePermission) {
        logger.debug("rolePermission findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(rolePermission, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<RolePermission> rolePermissions = rolePermissionDao.findByObject(rolePermission);

        logger.debug("查询结果， {}", JSON.toJSONString(rolePermissions, true));
        return new PageInfo<>(rolePermissions);
    }

    @Override
    public RolePermission findById(Long rolePermissionId) {
        logger.debug("rolePermission findById, 参数 rolePermissionId: {}", rolePermissionId);
        RolePermission rolePermission = rolePermissionDao.selectById(rolePermissionId);

        logger.debug("查询结果： {}", JSON.toJSONString(rolePermission, true));
        return rolePermission;
    }

    @Override
    public int deleteById(Long rolePermissionId, Long operatorId) {
        logger.debug("rolePermission 删除， 参数 rolePermissionId : {}", rolePermissionId);
        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(rolePermissionId);
        rolePermission.setIsDel(DelFlagEnum.YES.getCode());
        rolePermission.setUpdateTime(new Date());
        rolePermission.setUpdateRolePermissionId(operatorId);
        int num = rolePermissionDao.updateById(rolePermission);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
