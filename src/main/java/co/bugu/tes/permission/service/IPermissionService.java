package co.bugu.tes.permission.service;

import co.bugu.tes.permission.domain.Permission;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-19 19:29
 */
public interface IPermissionService {

    /**
     * 新增
     *
     * @param permission
     * @return
     */
    long add(Permission permission);

    /**
     * 通过id更新
     *
     * @param permission
     * @return
     */
    int updateById(Permission permission);

    /**
     * 条件查询
     *
     * @param permission
     * @return
     */
    List<Permission> findByCondition(Permission permission);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param permission     查询条件
     * @return
     */
    List<Permission> findByCondition(Integer pageNum, Integer pageSize, Permission permission);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param permission     查询条件
     * @return
     */
    PageInfo<Permission> findByConditionWithPage(Integer pageNum, Integer pageSize, Permission permission);

    /**
     * 通过id查询
     *
     * @param permissionId
     * @return
     */
    Permission findById(Long permissionId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param permissionId
     * @return
     */
    int deleteById(Long permissionId, Long operatorId);

}
