package co.bugu.tes.answer.service;

import co.bugu.tes.answer.domain.Answer;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
public interface IAnswerService {

    /**
     * 新增
     *
     * @param answer
     * @return
     */
    long add(Answer answer);

    /**
     * 通过id更新
     *
     * @param answer
     * @return
     */
    int updateById(Answer answer);

    /**
     * 条件查询
     *
     * @param answer
     * @return
     */
    List<Answer> findByCondition(Answer answer);


    List<Answer> findByCondition(Answer answer, String orderBy);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param answer     查询条件
     * @return
     */
    List<Answer> findByCondition(Integer pageNum, Integer pageSize, Answer answer);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param answer     查询条件
     * @return
     */
    PageInfo<Answer> findByConditionWithPage(Integer pageNum, Integer pageSize, Answer answer);

    /**
     * 通过id查询
     *
     * @param answerId
     * @return
     */
    Answer findById(Long answerId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param answerId
     * @return
     */
    int deleteById(Long answerId, Long operatorId);

}
