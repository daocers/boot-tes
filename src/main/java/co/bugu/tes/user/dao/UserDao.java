package co.bugu.tes.user.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.user.domain.User;

import java.util.List;

public interface UserDao extends BaseDao<User> {
    /**
     * 批量添加用户
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/1/5 11:54
     */
    void batchAdd(List<User> userList);

    /**
     * 查找管理的机构、岗位、部门下的用户
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/12 14:55
     */
    List<User> findUserUnderManage(User user);
}
