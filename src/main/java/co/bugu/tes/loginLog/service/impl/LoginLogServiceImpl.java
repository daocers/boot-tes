package co.bugu.tes.loginLog.service.impl;

import co.bugu.tes.loginLog.dao.LoginLogDao;
import co.bugu.tes.loginLog.domain.LoginLog;
import co.bugu.tes.loginLog.service.ILoginLogService;
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
 * @create 2018-12-04 10:20
 */
@Service
public class LoginLogServiceImpl implements ILoginLogService {
    @Autowired
    LoginLogDao loginLogDao;

    private Logger logger = LoggerFactory.getLogger(LoginLogServiceImpl.class);

    private static String ORDER_BY = "id desc";

    @Override
    public long add(LoginLog loginLog) {
        //todo 校验参数
        Date now = new Date();
        loginLog.setCreateTime(now);
        loginLog.setUpdateTime(now);
        loginLogDao.insert(loginLog);
        return loginLog.getId();
    }

    @Override
    public int updateById(LoginLog loginLog) {
        logger.debug("loginLog updateById, 参数： {}", JSON.toJSONString(loginLog, true));
        Preconditions.checkNotNull(loginLog.getId(), "id不能为空");
        loginLog.setUpdateTime(new Date());
        return loginLogDao.updateById(loginLog);
    }

    @Override
    public List<LoginLog> findByCondition(LoginLog loginLog) {
        logger.debug("loginLog findByCondition, 参数： {}", JSON.toJSONString(loginLog, true));
        PageHelper.orderBy(ORDER_BY);
        List<LoginLog> loginLogs = loginLogDao.findByObject(loginLog);

        logger.debug("查询结果， {}", JSON.toJSONString(loginLogs, true));
        return loginLogs;
    }

    @Override
    public List<LoginLog> findByCondition(Integer pageNum, Integer pageSize, LoginLog loginLog) {
        logger.debug("loginLog findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(loginLog, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<LoginLog> loginLogs = loginLogDao.findByObject(loginLog);

        logger.debug("查询结果， {}", JSON.toJSONString(loginLogs, true));
        return loginLogs;
    }

    @Override
    public PageInfo<LoginLog> findByConditionWithPage(Integer pageNum, Integer pageSize, LoginLog loginLog) {
        logger.debug("loginLog findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(loginLog, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<LoginLog> loginLogs = loginLogDao.findByObject(loginLog);

        logger.debug("查询结果， {}", JSON.toJSONString(loginLogs, true));
        return new PageInfo<>(loginLogs);
    }

    @Override
    public LoginLog findById(Long loginLogId) {
        logger.debug("loginLog findById, 参数 loginLogId: {}", loginLogId);
        LoginLog loginLog = loginLogDao.selectById(loginLogId);

        logger.debug("查询结果： {}", JSON.toJSONString(loginLog, true));
        return loginLog;
    }

    @Override
    public int deleteById(Long loginLogId, Long operatorId) {
        logger.debug("loginLog 删除， 参数 loginLogId : {}", loginLogId);
        LoginLog loginLog = new LoginLog();
        loginLog.setId(loginLogId);
        loginLog.setUpdateTime(new Date());
        loginLog.setUpdateUserId(operatorId);
        int num = loginLogDao.updateById(loginLog);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
