package co.bugu.tes.receiptAnswer.service;

import co.bugu.tes.receiptAnswer.domain.ReceiptAnswer;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2019-04-04 16:33
 */
public interface IReceiptAnswerService {

    /**
     * 新增
     *
     * @param receiptAnswer
     * @return
     */
    long add(ReceiptAnswer receiptAnswer);

    /**
     * 通过id更新
     *
     * @param receiptAnswer
     * @return
     */
    int updateById(ReceiptAnswer receiptAnswer);

    /**
     * 条件查询
     *
     * @param receiptAnswer
     * @return
     */
    List<ReceiptAnswer> findByCondition(ReceiptAnswer receiptAnswer);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param receiptAnswer     查询条件
     * @return
     */
    List<ReceiptAnswer> findByCondition(Integer pageNum, Integer pageSize, ReceiptAnswer receiptAnswer);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param receiptAnswer     查询条件
     * @return
     */
    PageInfo<ReceiptAnswer> findByConditionWithPage(Integer pageNum, Integer pageSize, ReceiptAnswer receiptAnswer);

    /**
     * 通过id查询
     *
     * @param receiptAnswerId
     * @return
     */
    ReceiptAnswer findById(Long receiptAnswerId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param receiptAnswerId
     * @return
     */
    int deleteById(Long receiptAnswerId, Long operatorId);

    /**
     * 批量添加数据
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/4/24 11:35
     */
    List<ReceiptAnswer> batchAdd(List<ReceiptAnswer> list);
}
