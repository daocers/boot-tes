package co.bugu.tes.rolePermissionX.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.rolePermissionX.dao.RolePermissionXDao;
import co.bugu.tes.rolePermissionX.domain.RolePermissionX;
import co.bugu.tes.rolePermissionX.service.IRolePermissionXService;
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
 * @create 2018-11-20 17:15
 */
@Service
public class RolePermissionXServiceImpl implements IRolePermissionXService {
    @Autowired
    RolePermissionXDao rolePermissionXDao;

    private Logger logger = LoggerFactory.getLogger(RolePermissionXServiceImpl.class);

    private static String ORDER_BY = "id desc";

    @Override
    public long add(RolePermissionX rolePermissionX) {
        //todo 校验参数
        rolePermissionX.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        rolePermissionX.setCreateTime(now);
        rolePermissionX.setUpdateTime(now);
        rolePermissionXDao.insert(rolePermissionX);
        return rolePermissionX.getId();
    }

    @Override
    public int updateById(RolePermissionX rolePermissionX) {
        logger.debug("rolePermissionX updateById, 参数： {}", JSON.toJSONString(rolePermissionX, true));
        Preconditions.checkNotNull(rolePermissionX.getId(), "id不能为空");
        rolePermissionX.setUpdateTime(new Date());
        return rolePermissionXDao.updateById(rolePermissionX);
    }

    @Override
    public List<RolePermissionX> findByCondition(RolePermissionX rolePermissionX) {
        logger.debug("rolePermissionX findByCondition, 参数： {}", JSON.toJSONString(rolePermissionX, true));
        PageHelper.orderBy(ORDER_BY);
        List<RolePermissionX> rolePermissionXs = rolePermissionXDao.findByObject(rolePermissionX);

        logger.debug("查询结果， {}", JSON.toJSONString(rolePermissionXs, true));
        return rolePermissionXs;
    }

    @Override
    public List<RolePermissionX> findByCondition(Integer pageNum, Integer pageSize, RolePermissionX rolePermissionX) {
        logger.debug("rolePermissionX findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(rolePermissionX, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<RolePermissionX> rolePermissionXs = rolePermissionXDao.findByObject(rolePermissionX);

        logger.debug("查询结果， {}", JSON.toJSONString(rolePermissionXs, true));
        return rolePermissionXs;
    }

    @Override
    public PageInfo<RolePermissionX> findByConditionWithPage(Integer pageNum, Integer pageSize, RolePermissionX rolePermissionX) {
        logger.debug("rolePermissionX findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(rolePermissionX, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<RolePermissionX> rolePermissionXs = rolePermissionXDao.findByObject(rolePermissionX);

        logger.debug("查询结果， {}", JSON.toJSONString(rolePermissionXs, true));
        return new PageInfo<>(rolePermissionXs);
    }

    @Override
    public RolePermissionX findById(Long rolePermissionXId) {
        logger.debug("rolePermissionX findById, 参数 rolePermissionXId: {}", rolePermissionXId);
        RolePermissionX rolePermissionX = rolePermissionXDao.selectById(rolePermissionXId);

        logger.debug("查询结果： {}", JSON.toJSONString(rolePermissionX, true));
        return rolePermissionX;
    }

    @Override
    public int deleteById(Long rolePermissionXId, Long operatorId) {
        logger.debug("rolePermissionX 删除， 参数 rolePermissionXId : {}", rolePermissionXId);
        RolePermissionX rolePermissionX = new RolePermissionX();
        rolePermissionX.setId(rolePermissionXId);
        rolePermissionX.setIsDel(DelFlagEnum.YES.getCode());
        rolePermissionX.setUpdateTime(new Date());
        rolePermissionX.setUpdateUserId(operatorId);
        int num = rolePermissionXDao.updateById(rolePermissionX);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
