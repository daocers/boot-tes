package co.bugu.tes.receipt.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.receipt.domain.Receipt;

import java.util.List;

public interface ReceiptDao extends BaseDao<Receipt> {
    /**
     * 删除指定sceneId的所有凭条信息
     *
     * @param
     * @return
     * @author daocers
     * @data 2019/4/4 16:57
     */
    int deleteBySceneId(Long sceneId);

    /**
     * 批量添加
     *
     * @param
     * @return
     * @author daocers
     * @data 2019/4/4 17:24
     */
    void batchAdd(List<Receipt> receipts);
}
