package co.bugu.tes.rolePermissionX.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.rolePermissionX.domain.RolePermissionX;

import java.util.List;

public interface RolePermissionXDao extends BaseDao<RolePermissionX>{
    int deleteByRoleId(Long roleId);

    void batchAdd(List<RolePermissionX> xList);
}
