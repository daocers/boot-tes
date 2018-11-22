package co.bugu.tes.userRoleX.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.userRoleX.domain.UserRoleX;

import java.util.List;

public interface UserRoleXDao extends BaseDao<UserRoleX>{
    void batchAdd(List<UserRoleX> xList);

    void deleteByUserId(Long userId);
}
