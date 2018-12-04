package co.bugu.tes.loginLog.service;

import co.bugu.tes.loginLog.domain.LoginLog;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-12-04 10:20
 */
public interface ILoginLogService {

    /**
     * 新增
     *
     * @param loginLog
     * @return
     */
    long add(LoginLog loginLog);

    /**
     * 通过id更新
     *
     * @param loginLog
     * @return
     */
    int updateById(LoginLog loginLog);

    /**
     * 条件查询
     *
     * @param loginLog
     * @return
     */
    List<LoginLog> findByCondition(LoginLog loginLog);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param loginLog     查询条件
     * @return
     */
    List<LoginLog> findByCondition(Integer pageNum, Integer pageSize, LoginLog loginLog);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param loginLog     查询条件
     * @return
     */
    PageInfo<LoginLog> findByConditionWithPage(Integer pageNum, Integer pageSize, LoginLog loginLog);

    /**
     * 通过id查询
     *
     * @param loginLogId
     * @return
     */
    LoginLog findById(Long loginLogId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param loginLogId
     * @return
     */
    int deleteById(Long loginLogId, Long operatorId);

}
