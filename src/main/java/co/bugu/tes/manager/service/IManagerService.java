package co.bugu.tes.manager.service;

import co.bugu.exception.UserException;
import co.bugu.tes.manager.domain.Manager;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-12-14 09:14
 */
public interface IManagerService {

    /**
     * 新增
     *
     * @param manager
     * @return
     */
    long add(Manager manager);

    /**
     * 通过id更新
     *
     * @param manager
     * @return
     */
    int updateById(Manager manager);

    /**
     * 条件查询
     *
     * @param manager
     * @return
     */
    List<Manager> findByCondition(Manager manager);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param manager     查询条件
     * @return
     */
    List<Manager> findByCondition(Integer pageNum, Integer pageSize, Manager manager);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param manager     查询条件
     * @return
     */
    PageInfo<Manager> findByConditionWithPage(Integer pageNum, Integer pageSize, Manager manager);

    /**
     * 通过id查询
     *
     * @param managerId
     * @return
     */
    Manager findById(Long managerId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param managerId
     * @return
     */
    int deleteById(Long managerId, Long operatorId);


    int setManager(int type, Long userId, Long targetId) throws UserException;

}
