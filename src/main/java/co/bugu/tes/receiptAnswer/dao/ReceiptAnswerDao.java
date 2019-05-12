package co.bugu.tes.receiptAnswer.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.receiptAnswer.domain.ReceiptAnswer;

import java.util.List;

public interface ReceiptAnswerDao extends BaseDao<ReceiptAnswer> {

    void batchAdd(List<ReceiptAnswer> list);

    /**
     * 删除特定场次特定用户的凭条信息
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/10 23:10
     */
    int updateBySceneIdAndUserId(ReceiptAnswer receiptAnswer);
}
