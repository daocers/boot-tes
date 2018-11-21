package co.bugu.tes.single.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.single.domain.Single;

import java.util.List;

public interface SingleDao extends BaseDao<Single>{
    /**
     * 批量添加
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/21 14:25
     */
    void batchAdd(List<Single> singles);
}
