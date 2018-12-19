package co.bugu.tes.receiptRecord.service;

import co.bugu.tes.receiptRecord.domain.ReceiptRecord;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-12-19 09:56
 */
public interface IReceiptRecordService {

    /**
     * 新增
     *
     * @param receiptRecord
     * @return
     */
    long add(ReceiptRecord receiptRecord);

    /**
     * 通过id更新
     *
     * @param receiptRecord
     * @return
     */
    int updateById(ReceiptRecord receiptRecord);

    /**
     * 条件查询
     *
     * @param receiptRecord
     * @return
     */
    List<ReceiptRecord> findByCondition(ReceiptRecord receiptRecord);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param receiptRecord     查询条件
     * @return
     */
    List<ReceiptRecord> findByCondition(Integer pageNum, Integer pageSize, ReceiptRecord receiptRecord);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param receiptRecord     查询条件
     * @return
     */
    PageInfo<ReceiptRecord> findByConditionWithPage(Integer pageNum, Integer pageSize, ReceiptRecord receiptRecord);

    /**
     * 通过id查询
     *
     * @param receiptRecordId
     * @return
     */
    ReceiptRecord findById(Long receiptRecordId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param receiptRecordId
     * @return
     */
    int deleteById(Long receiptRecordId, Long operatorId);

}
