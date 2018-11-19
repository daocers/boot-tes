package co.bugu.tes.rolePermission.service;

import co.bugu.tes.rolePermission.domain.RolePermission;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-19 19:29
 */
public interface IRolePermissionService {

    /**
     * 新增
     *
     * @param rolePermission
     * @return
     */
    long add(RolePermission rolePermission);

    /**
     * 通过id更新
     *
     * @param rolePermission
     * @return
     */
    int updateById(RolePermission rolePermission);

    /**
     * 条件查询
     *
     * @param rolePermission
     * @return
     */
    List<RolePermission> findByCondition(RolePermission rolePermission);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param rolePermission     查询条件
     * @return
     */
    List<RolePermission> findByCondition(Integer pageNum, Integer pageSize, RolePermission rolePermission);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param rolePermission     查询条件
     * @return
     */
    PageInfo<RolePermission> findByConditionWithPage(Integer pageNum, Integer pageSize, RolePermission rolePermission);

    /**
     * 通过id查询
     *
     * @param rolePermissionId
     * @return
     */
    RolePermission findById(Long rolePermissionId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param rolePermissionId
     * @return
     */
    int deleteById(Long rolePermissionId, Long operatorId);

}
