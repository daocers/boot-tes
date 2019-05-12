package co.bugu.tes.manager.service.impl;

import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.manager.dao.ManagerDao;
import co.bugu.tes.manager.domain.Manager;
import co.bugu.tes.manager.service.IManagerService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-12-14 09:14
 */
@Service
public class ManagerServiceImpl implements IManagerService {
    @Autowired
    ManagerDao managerDao;
    @Autowired
    IUserService userService;

    private Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

    private static String ORDER_BY = "id desc";

    @Override
    public long add(Manager manager) {
        //todo 校验参数
        manager.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        manager.setCreateTime(now);
        manager.setUpdateTime(now);
        managerDao.insert(manager);
        return manager.getId();
    }

    @Override
    public int updateById(Manager manager) {
        logger.debug("manager updateById, 参数： {}", JSON.toJSONString(manager, true));
        Preconditions.checkNotNull(manager.getId(), "id不能为空");
        manager.setUpdateTime(new Date());
        return managerDao.updateById(manager);
    }

    @Override
    public List<Manager> findByCondition(Manager manager) {
        logger.debug("manager findByCondition, 参数： {}", JSON.toJSONString(manager, true));
        PageHelper.orderBy(ORDER_BY);
        List<Manager> managers = managerDao.findByObject(manager);

        logger.debug("查询结果， {}", JSON.toJSONString(managers, true));
        return managers;
    }

    @Override
    public List<Manager> findByCondition(Integer pageNum, Integer pageSize, Manager manager) {
        logger.debug("manager findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(manager, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Manager> managers = managerDao.findByObject(manager);

        logger.debug("查询结果， {}", JSON.toJSONString(managers, true));
        return managers;
    }

    @Override
    public PageInfo<Manager> findByConditionWithPage(Integer pageNum, Integer pageSize, Manager manager) {
        logger.debug("manager findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(manager, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Manager> managers = managerDao.findByObject(manager);

        logger.debug("查询结果， {}", JSON.toJSONString(managers, true));
        return new PageInfo<>(managers);
    }

    @Override
    public Manager findById(Long managerId) {
        logger.debug("manager findById, 参数 managerId: {}", managerId);
        Manager manager = managerDao.selectById(managerId);

        logger.debug("查询结果： {}", JSON.toJSONString(manager, true));
        return manager;
    }

    @Override
    public int deleteById(Long managerId, Long operatorId) {
        logger.debug("manager 删除， 参数 managerId : {}", managerId);
        Manager manager = new Manager();
        manager.setId(managerId);
        manager.setIsDel(DelFlagEnum.YES.getCode());
        manager.setUpdateTime(new Date());
        manager.setUpdateUserId(operatorId);
        int num = managerDao.updateById(manager);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 3000)
    public int setManager(int type, Long userId, Long targetId) throws UserException {
        Manager query = new Manager();
        query.setType(type);
        query.setUserId(userId);
        query.setTargetId(targetId);
        List<Manager> list = managerDao.findByObject(query);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Manager manager : list) {
                if (manager.getStatus() != BaseStatusEnum.ENABLE.getCode()) {
                    manager.setStatus(BaseStatusEnum.ENABLE.getCode());
                    managerDao.updateById(manager);
                }
            }
        } else {
            Long currentUserId = UserUtil.getCurrentUser().getId();
            Manager manager = new Manager();
            manager.setStatus(BaseStatusEnum.ENABLE.getCode());
            manager.setTargetId(targetId);
            manager.setUserId(userId);
            manager.setType(type);
            manager.setIsDel(DelFlagEnum.NO.getCode());
            manager.setCreateUserId(currentUserId);
            manager.setUpdateUserId(currentUserId);
            managerDao.insert(manager);
        }
        return 1;
    }

    @Override
    public List<User> getManager(int type, Long targetId) {
        Manager query = new Manager();
        query.setIsDel(DelFlagEnum.NO.getCode());
        query.setType(type);
        query.setTargetId(targetId);

        List<Manager> managers = managerDao.findByObject(query);
        if (CollectionUtils.isNotEmpty(managers)) {
            List<User> userList = Lists.transform(managers, new Function<Manager, User>() {
                @Override
                public User apply(@Nullable Manager manager) {
                    Long userId = manager.getUserId();
                    User user = userService.findById(userId);
                    return user;
                }
            });
            return userList;
        }
        return new ArrayList<>();
    }

}
