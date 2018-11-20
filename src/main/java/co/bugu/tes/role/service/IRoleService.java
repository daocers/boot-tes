package co.bugu.tes.role.service;

import co.bugu.tes.role.domain.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
public interface IRoleService {

    /**
     * 新增
     *
     * @param role
     * @return
     */
    long add(Role role);

    /**
     * 通过id更新
     *
     * @param role
     * @return
     */
    int updateById(Role role);

    /**
     * 条件查询
     *
     * @param role
     * @return
     */
    List<Role> findByCondition(Role role);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param role     查询条件
     * @return
     */
    List<Role> findByCondition(Integer pageNum, Integer pageSize, Role role);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param role     查询条件
     * @return
     */
    PageInfo<Role> findByConditionWithPage(Integer pageNum, Integer pageSize, Role role);

    /**
     * 通过id查询
     *
     * @param roleId
     * @return
     */
    Role findById(Long roleId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param roleId
     * @return
     */
    int deleteById(Long roleId, Long operatorId);

}
