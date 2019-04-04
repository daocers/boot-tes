package co.bugu.tes.receipt.service;

import co.bugu.tes.receipt.domain.Receipt;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2019-04-04 16:33
 */
public interface IReceiptService {

    /**
     * 新增
     *
     * @param receipt
     * @return
     */
    long add(Receipt receipt);

    /**
     * 通过id更新
     *
     * @param receipt
     * @return
     */
    int updateById(Receipt receipt);

    /**
     * 条件查询
     *
     * @param receipt
     * @return
     */
    List<Receipt> findByCondition(Receipt receipt);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param receipt  查询条件
     * @return
     */
    List<Receipt> findByCondition(Integer pageNum, Integer pageSize, Receipt receipt);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param receipt  查询条件
     * @return
     */
    PageInfo<Receipt> findByConditionWithPage(Integer pageNum, Integer pageSize, Receipt receipt);

    /**
     * 通过id查询
     *
     * @param receiptId
     * @return
     */
    Receipt findById(Long receiptId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param receiptId
     * @return
     */
    int deleteById(Long receiptId, Long operatorId);

    /**
     * 保存凭条信息
     *
     * @param
     * @return
     * @author daocers
     * @data 2019/4/4 16:50
     */
    List<Receipt> save(Long sceneId, Integer receiptCount, Integer numberLength);
}
