package co.bugu.tes.userRoleX.service;

import co.bugu.tes.userRoleX.domain.UserRoleX;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
public interface IUserRoleXService {

    /**
     * 新增
     *
     * @param userRoleX
     * @return
     */
    long add(UserRoleX userRoleX);

    /**
     * 通过id更新
     *
     * @param userRoleX
     * @return
     */
    int updateById(UserRoleX userRoleX);

    /**
     * 条件查询
     *
     * @param userRoleX
     * @return
     */
    List<UserRoleX> findByCondition(UserRoleX userRoleX);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param userRoleX     查询条件
     * @return
     */
    List<UserRoleX> findByCondition(Integer pageNum, Integer pageSize, UserRoleX userRoleX);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param userRoleX     查询条件
     * @return
     */
    PageInfo<UserRoleX> findByConditionWithPage(Integer pageNum, Integer pageSize, UserRoleX userRoleX);

    /**
     * 通过id查询
     *
     * @param userRoleXId
     * @return
     */
    UserRoleX findById(Long userRoleXId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param userRoleXId
     * @return
     */
    int deleteById(Long userRoleXId, Long operatorId);

    /**
     * 删除用户的角色关联信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/22 19:20
     */
    void deleteByUserId(Long userId);

    /**
     * 批量添加用户角色信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/22 19:20
     */
    List<UserRoleX> batchAdd(List<UserRoleX> xList);
}
