package co.bugu.tes.role.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.role.dao.RoleDao;
import co.bugu.tes.role.domain.Role;
import co.bugu.tes.role.service.IRoleService;
import co.bugu.tes.userRoleX.dao.UserRoleXDao;
import co.bugu.tes.userRoleX.domain.UserRoleX;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-11-19 19:29
 */
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleDao roleDao;
    @Autowired
    UserRoleXDao userRoleXDao;

    private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

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
    public List<Role> findByUserId(Long userId) {
        UserRoleX query = new UserRoleX();
        query.setUserId(userId);
        List<UserRoleX> xList = userRoleXDao.findByObject(query);
        List<Role> list = new ArrayList<>(xList.size());

        for (UserRoleX x : xList) {
            Long roleId = x.getRoleId();
            Role role = roleDao.selectById(roleId);
            list.add(role);
        }
        return list;
    }

}
