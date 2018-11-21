package co.bugu.tes.multi.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.multi.domain.Multi;

import java.util.List;

public interface MultiDao extends BaseDao<Multi>{
    void batchAdd(List<Multi> multis);
}
