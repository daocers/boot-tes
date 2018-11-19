package co.bugu.tes.questionBank.service;

import co.bugu.tes.questionBank.domain.QuestionBank;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-19 17:52
 */
public interface IQuestionBankService {

    /**
     * 新增
     *
     * @param questionBank
     * @return
     */
    long add(QuestionBank questionBank);

    /**
     * 通过id更新
     *
     * @param questionBank
     * @return
     */
    int updateById(QuestionBank questionBank);

    /**
     * 条件查询
     *
     * @param questionBank
     * @return
     */
    List<QuestionBank> findByCondition(QuestionBank questionBank);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param questionBank     查询条件
     * @return
     */
    List<QuestionBank> findByCondition(Integer pageNum, Integer pageSize, QuestionBank questionBank);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param questionBank     查询条件
     * @return
     */
    PageInfo<QuestionBank> findByConditionWithPage(Integer pageNum, Integer pageSize, QuestionBank questionBank);

    /**
     * 通过id查询
     *
     * @param questionBankId
     * @return
     */
    QuestionBank findById(Long questionBankId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param questionBankId
     * @return
     */
    int deleteById(Long questionBankId, Long operatorId);

}
