package co.bugu.tes.role.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.role.dao.RoleDao;
import co.bugu.tes.role.domain.Role;
import co.bugu.tes.role.service.IRoleService;
import co.bugu.tes.rolePermissionX.dao.RolePermissionXDao;
import co.bugu.tes.rolePermissionX.domain.RolePermissionX;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleDao roleDao;
    @Autowired
    RolePermissionXDao xDao;

    private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private static String ORDER_BY = "id desc";

    @Override
    public long add(Role role) {
        //todo 校验参数
        role.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        role.setCreateTime(now);
        role.setUpdateTime(now);
        roleDao.insert(role);
        return role.getId();
    }

    @Override
    public int updateById(Role role) {
        logger.debug("role updateById, 参数： {}", JSON.toJSONString(role, true));
        Preconditions.checkNotNull(role.getId(), "id不能为空");
        role.setUpdateTime(new Date());
        return roleDao.updateById(role);
    }

    @Override
    public List<Role> findByCondition(Role role) {
        logger.debug("role findByCondition, 参数： {}", JSON.toJSONString(role, true));
        PageHelper.orderBy(ORDER_BY);
        List<Role> roles = roleDao.findByObject(role);

        logger.debug("查询结果， {}", JSON.toJSONString(roles, true));
        return roles;
    }

    @Override
    public List<Role> findByCondition(Integer pageNum, Integer pageSize, Role role) {
        logger.debug("role findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(role, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Role> roles = roleDao.findByObject(role);

        logger.debug("查询结果， {}", JSON.toJSONString(roles, true));
        return roles;
    }

    @Override
    public PageInfo<Role> findByConditionWithPage(Integer pageNum, Integer pageSize, Role role) {
        logger.debug("role findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(role, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Role> roles = roleDao.findByObject(role);

        logger.debug("查询结果， {}", JSON.toJSONString(roles, true));
        return new PageInfo<>(roles);
    }

    @Override
    public Role findById(Long roleId) {
        logger.debug("role findById, 参数 roleId: {}", roleId);
        Role role = roleDao.selectById(roleId);

        logger.debug("查询结果： {}", JSON.toJSONString(role, true));
        return role;
    }

    @Override
    public int deleteById(Long roleId, Long operatorId) {
        logger.debug("role 删除， 参数 roleId : {}", roleId);
        Role role = new Role();
        role.setId(roleId);
        role.setIsDel(DelFlagEnum.YES.getCode());
        role.setUpdateTime(new Date());
        role.setUpdateUserId(operatorId);
        int num = roleDao.updateById(role);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 2000, isolation = Isolation.READ_COMMITTED)
    public List<RolePermissionX> authorize(Long roleId, List<Long> permissionIdList, Long userId) {
        Date now = new Date();
        int num = xDao.deleteByRoleId(roleId);

        List<RolePermissionX> xList = new ArrayList<>();
        for(Long id: permissionIdList){
            RolePermissionX x = new RolePermissionX();
            x.setRoleId(roleId);
            x.setIsDel(DelFlagEnum.NO.getCode());
            x.setCreateUserId(userId);
            x.setUpdateUserId(userId);
            x.setPermissionId(id);
            x.setNo(1);
            xList.add(x);
        }
        xDao.batchAdd(xList);
        return xList;
    }
}
