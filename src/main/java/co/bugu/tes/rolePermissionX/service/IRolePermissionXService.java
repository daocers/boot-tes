package co.bugu.tes.rolePermissionX.service;

import co.bugu.tes.rolePermissionX.domain.RolePermissionX;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
public interface IRolePermissionXService {

    /**
     * 新增
     *
     * @param rolePermissionX
     * @return
     */
    long add(RolePermissionX rolePermissionX);

    /**
     * 通过id更新
     *
     * @param rolePermissionX
     * @return
     */
    int updateById(RolePermissionX rolePermissionX);

    /**
     * 条件查询
     *
     * @param rolePermissionX
     * @return
     */
    List<RolePermissionX> findByCondition(RolePermissionX rolePermissionX);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param rolePermissionX     查询条件
     * @return
     */
    List<RolePermissionX> findByCondition(Integer pageNum, Integer pageSize, RolePermissionX rolePermissionX);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param rolePermissionX     查询条件
     * @return
     */
    PageInfo<RolePermissionX> findByConditionWithPage(Integer pageNum, Integer pageSize, RolePermissionX rolePermissionX);

    /**
     * 通过id查询
     *
     * @param rolePermissionXId
     * @return
     */
    RolePermissionX findById(Long rolePermissionXId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param rolePermissionXId
     * @return
     */
    int deleteById(Long rolePermissionXId, Long operatorId);

}
