package co.bugu.tes.judge.service;

import co.bugu.tes.judge.domain.Judge;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
public interface IJudgeService {

    /**
     * 新增
     *
     * @param judge
     * @return
     */
    long add(Judge judge);

    /**
     * 通过id更新
     *
     * @param judge
     * @return
     */
    int updateById(Judge judge);

    /**
     * 条件查询
     *
     * @param judge
     * @return
     */
    List<Judge> findByCondition(Judge judge);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param judge     查询条件
     * @return
     */
    List<Judge> findByCondition(Integer pageNum, Integer pageSize, Judge judge);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param judge     查询条件
     * @return
     */
    PageInfo<Judge> findByConditionWithPage(Integer pageNum, Integer pageSize, Judge judge);

    /**
     * 通过id查询
     *
     * @param judgeId
     * @return
     */
    Judge findById(Long judgeId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param judgeId
     * @return
     */
    int deleteById(Long judgeId, Long operatorId);


    /**
     * 批量添加判断题
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/21 17:34
     */
    List<Judge> batchAdd(List<List<String>> data, long userId, Long bankId, Long stationId, Long branchId, Long departmentId, Integer publicFlag);

    /**
     * 获取指定题型的id列表
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/25 21:55
     */
    List<Long> getAllIds(Judge judge);
}
