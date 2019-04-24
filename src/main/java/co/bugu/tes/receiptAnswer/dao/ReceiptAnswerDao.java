package co.bugu.tes.receiptAnswer.dao;

import co.bugu.common.dao.BaseDao;
import co.bugu.tes.receiptAnswer.domain.ReceiptAnswer;

import java.util.List;

public interface ReceiptAnswerDao extends BaseDao<ReceiptAnswer> {

    void batchAdd(List<ReceiptAnswer> list);
}
