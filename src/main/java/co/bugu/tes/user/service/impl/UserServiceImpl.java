package co.bugu.tes.user.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.service.IBranchService;
import co.bugu.tes.department.domain.Department;
import co.bugu.tes.department.service.IDepartmentService;
import co.bugu.tes.station.domain.Station;
import co.bugu.tes.station.service.IStationService;
import co.bugu.tes.user.dao.UserDao;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserDao userDao;
    @Autowired
    IDepartmentService departmentService;
    @Autowired
    IBranchService branchService;
    @Autowired
    IStationService stationService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Value("${bugu.init.password:888888}")
    private String initPassword;

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

    @Override
    public List<User> batchAdd(List<List<String>> data) throws UserException {
        logger.info("批量添加用户信息， {}", JSON.toJSONString(data, true));
        if (CollectionUtils.isNotEmpty(data)) {
            List<User> userList = new ArrayList<>();
            if (data.size() > 1) {
                Long userId = UserUtil.getCurrentUser().getId();
                List<Department> departments = departmentService.findByCondition(null);
                List<Branch> branches = branchService.findByCondition(null);
                List<Station> stations = stationService.findByCondition(null);

                Map<String, Long> departInfo = new HashMap<>();
                for (Department department : departments) {
                    departInfo.put(department.getName(), department.getId());
                }
                Map<String, Long> stationInfo = new HashMap<>();
                for (Station station : stations) {
                    stationInfo.put(station.getName(), station.getId());
                }
                Map<String, Long> branchInfo = new HashMap<>();
                for (Branch branch : branches) {
                    branchInfo.put(branch.getName(), branch.getId());
                }
                for (int i = 0; i < data.size(); i++) {
                    List<String> line = data.get(i);
                    String name = line.get(0);
                    String username = line.get(1);
                    String idNo = line.get(2);
                    String branchName = line.get(3);
                    String departmentName = line.get(4);
                    String stationName = line.get(5);
                    Preconditions.checkArgument(StringUtils.isNotEmpty(name), "第" + (i + 2) + "行姓名不能为空");
                    Preconditions.checkArgument(StringUtils.isNotEmpty(username), "第" + (i + 2) + "行工号不能为空");
                    if (StringUtils.isNotEmpty(branchName)) {
                        Preconditions.checkArgument(branchInfo.containsKey(branchName), "第" + (i + 2) + "行分行名称不存在");
                    }
                    if (StringUtils.isNotEmpty(stationName)) {
                        Preconditions.checkArgument(stationInfo.containsKey(stationName), "第" + (i + 2) + "行岗位名称不存在");
                    }
                    if (StringUtils.isNotEmpty(departmentName)) {
                        Preconditions.checkArgument(departInfo.containsKey(departmentName), "第" + (i + 2) + "行部门不能不存在");
                    }
                    User user = new User();
                    user.setName(name);
                    user.setIdNo(idNo);
                    user.setUsername(username);
                    user.setPassword(initPassword);
                    user.setBranchId(branchInfo.get(branchName) == null ? -1L : branchInfo.get(branchName));
                    user.setStationId(stationInfo.get(stationName) == null ? -1L : stationInfo.get(stationName));
                    user.setDepartmentId(departInfo.get(departmentName) == null ? -1L : departInfo.get(departmentName));
                    user.setStatus(1);
                    user.setCreateUserId(userId);
                    user.setUpdateUserId(userId);
                    userList.add(user);
                }
                userDao.batchAdd(userList);
                return userList;

            }

        }
        return null;
    }

}
