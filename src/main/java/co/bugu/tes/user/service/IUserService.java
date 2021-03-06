package co.bugu.tes.user.service;

import co.bugu.exception.UserException;
import co.bugu.tes.user.domain.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
public interface IUserService {

    /**
     * 新增
     *
     * @param user
     * @return
     */
    long add(User user);

    /**
     * 通过id更新
     *
     * @param user
     * @return
     */
    int updateById(User user);

    /**
     * 条件查询
     *
     * @param user
     * @return
     */
    List<User> findByCondition(User user);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param user     查询条件
     * @return
     */
    List<User> findByCondition(Integer pageNum, Integer pageSize, User user);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param user     查询条件
     * @return
     */
    PageInfo<User> findByConditionWithPage(Integer pageNum, Integer pageSize, User user);

    /**
     * 通过id查询
     *
     * @param userId
     * @return
     */
    User findById(Long userId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param userId
     * @return
     */
    int deleteById(Long userId, Long operatorId);

    /**
     * 批量添加用户信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/1/5 11:26
     */
    List<User> batchAdd(List<List<String>> data, Long roleId) throws UserException;

    /**
     * 查找管理的机构中的用户
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/12 14:24
     */
    PageInfo<User> findUserUnderManage(Integer pageNum, Integer pageSize, User user);
}
