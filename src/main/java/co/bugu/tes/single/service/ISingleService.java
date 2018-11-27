package co.bugu.tes.single.service;

import co.bugu.tes.answer.dto.AnswerDto4GenPaper;
import co.bugu.tes.single.domain.Single;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
public interface ISingleService {

    /**
     * 新增
     *
     * @param single
     * @return
     */
    long add(Single single);

    /**
     * 通过id更新
     *
     * @param single
     * @return
     */
    int updateById(Single single);

    /**
     * 条件查询
     *
     * @param single
     * @return
     */
    List<Single> findByCondition(Single single);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param single     查询条件
     * @return
     */
    List<Single> findByCondition(Integer pageNum, Integer pageSize, Single single);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param single     查询条件
     * @return
     */
    PageInfo<Single> findByConditionWithPage(Integer pageNum, Integer pageSize, Single single);

    /**
     * 通过id查询
     *
     * @param singleId
     * @return
     */
    Single findById(Long singleId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param singleId
     * @return
     */
    int deleteById(Long singleId, Long operatorId);


    /**
     * 批量添加
     *
     * @param publicFlag  是否公开 1 公开， 2 私有
     * @param userId   操作的用户id
     * @return
     * @auther daocers
     * @date 2018/11/21 11:41
     */
    List<Single> batchAdd(List<List<String>> data, long userId, Long bankId, Long stationId, Long branchId, Long departmentId, Integer publicFlag);

    /**
     * 
     *
     * @param 
     * @return 
     * @auther daocers
     * @date 2018/11/25 22:06
     */
    List<AnswerDto4GenPaper> getAllIds(Single single);
}
