package co.bugu.tes.userRoleX.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.userRoleX.dao.UserRoleXDao;
import co.bugu.tes.userRoleX.domain.UserRoleX;
import co.bugu.tes.userRoleX.service.IUserRoleXService;
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
 * @create 2018-11-19 19:29
 */
@Service
public class UserRoleXServiceImpl implements IUserRoleXService {
    @Autowired
    UserRoleXDao userRoleXDao;

    private Logger logger = LoggerFactory.getLogger(UserRoleXServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(UserRoleX userRoleX) {
        //todo 校验参数
        userRoleX.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        userRoleX.setCreateTime(now);
        userRoleX.setUpdateTime(now);
        userRoleXDao.insert(userRoleX);
        return userRoleX.getId();
    }

    @Override
    public int updateById(UserRoleX userRoleX) {
        logger.debug("userRoleX updateById, 参数： {}", JSON.toJSONString(userRoleX, true));
        Preconditions.checkNotNull(userRoleX.getId(), "id不能为空");
        userRoleX.setUpdateTime(new Date());
        return userRoleXDao.updateById(userRoleX);
    }

    @Override
    public List<UserRoleX> findByCondition(UserRoleX userRoleX) {
        logger.debug("userRoleX findByCondition, 参数： {}", JSON.toJSONString(userRoleX, true));
        PageHelper.orderBy(ORDER_BY);
        List<UserRoleX> userRoleXs = userRoleXDao.findByObject(userRoleX);

        logger.debug("查询结果， {}", JSON.toJSONString(userRoleXs, true));
        return userRoleXs;
    }

    @Override
    public List<UserRoleX> findByCondition(Integer pageNum, Integer pageSize, UserRoleX userRoleX) {
        logger.debug("userRoleX findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(userRoleX, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<UserRoleX> userRoleXs = userRoleXDao.findByObject(userRoleX);

        logger.debug("查询结果， {}", JSON.toJSONString(userRoleXs, true));
        return userRoleXs;
    }

    @Override
    public PageInfo<UserRoleX> findByConditionWithPage(Integer pageNum, Integer pageSize, UserRoleX userRoleX) {
        logger.debug("userRoleX findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(userRoleX, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<UserRoleX> userRoleXs = userRoleXDao.findByObject(userRoleX);

        logger.debug("查询结果， {}", JSON.toJSONString(userRoleXs, true));
        return new PageInfo<>(userRoleXs);
    }

    @Override
    public UserRoleX findById(Long userRoleXId) {
        logger.debug("userRoleX findById, 参数 userRoleXId: {}", userRoleXId);
        UserRoleX userRoleX = userRoleXDao.selectById(userRoleXId);

        logger.debug("查询结果： {}", JSON.toJSONString(userRoleX, true));
        return userRoleX;
    }

    @Override
    public int deleteById(Long userRoleXId, Long operatorId) {
        logger.debug("userRoleX 删除， 参数 userRoleXId : {}", userRoleXId);
        UserRoleX userRoleX = new UserRoleX();
        userRoleX.setId(userRoleXId);
        userRoleX.setIsDel(DelFlagEnum.YES.getCode());
        userRoleX.setUpdateTime(new Date());
        userRoleX.setUpdateUserId(operatorId);
        int num = userRoleXDao.updateById(userRoleX);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
