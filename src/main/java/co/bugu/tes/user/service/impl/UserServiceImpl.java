package co.bugu.tes.user.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.user.dao.UserDao;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
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
public class UserServiceImpl implements IUserService {
    @Autowired
    UserDao userDao;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(User user) {
        //todo 校验参数
        user.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userDao.insert(user);
        return user.getId();
    }

    @Override
    public int updateById(User user) {
        logger.debug("user updateById, 参数： {}", JSON.toJSONString(user, true));
        Preconditions.checkNotNull(user.getId(), "id不能为空");
        user.setUpdateTime(new Date());
        return userDao.updateById(user);
    }

    @Override
    public List<User> findByCondition(User user) {
        logger.debug("user findByCondition, 参数： {}", JSON.toJSONString(user, true));
        PageHelper.orderBy(ORDER_BY);
        List<User> users = userDao.findByObject(user);

        logger.debug("查询结果， {}", JSON.toJSONString(users, true));
        return users;
    }

    @Override
    public List<User> findByCondition(Integer pageNum, Integer pageSize, User user) {
        logger.debug("user findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(user, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<User> users = userDao.findByObject(user);

        logger.debug("查询结果， {}", JSON.toJSONString(users, true));
        return users;
    }

    @Override
    public PageInfo<User> findByConditionWithPage(Integer pageNum, Integer pageSize, User user) {
        logger.debug("user findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(user, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<User> users = userDao.findByObject(user);

        logger.debug("查询结果， {}", JSON.toJSONString(users, true));
        return new PageInfo<>(users);
    }

    @Override
    public User findById(Long userId) {
        logger.debug("user findById, 参数 userId: {}", userId);
        User user = userDao.selectById(userId);

        logger.debug("查询结果： {}", JSON.toJSONString(user, true));
        return user;
    }

    @Override
    public int deleteById(Long userId, Long operatorId) {
        logger.debug("user 删除， 参数 userId : {}", userId);
        User user = new User();
        user.setId(userId);
        user.setIsDel(DelFlagEnum.YES.getCode());
        user.setUpdateTime(new Date());
        user.setUpdateUserId(operatorId);
        int num = userDao.updateById(user);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
