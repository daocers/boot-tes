package co.bugu.tes.paperPolicyCondition.service;

import co.bugu.tes.paperPolicyCondition.domain.PaperPolicyCondition;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2019-05-07 10:25
 */
public interface IPaperPolicyConditionService {

    /**
     * 新增
     *
     * @param paperPolicyCondition
     * @return
     */
    long add(PaperPolicyCondition paperPolicyCondition);

    /**
     * 通过id更新
     *
     * @param paperPolicyCondition
     * @return
     */
    int updateById(PaperPolicyCondition paperPolicyCondition);

    /**
     * 条件查询
     *
     * @param paperPolicyCondition
     * @return
     */
    List<PaperPolicyCondition> findByCondition(PaperPolicyCondition paperPolicyCondition);

    /**
     * 条件查询 分页
     *
     * @param pageNum              页码，从1 开始
     * @param pageSize             每页多少条
     * @param paperPolicyCondition 查询条件
     * @return
     */
    List<PaperPolicyCondition> findByCondition(Integer pageNum, Integer pageSize, PaperPolicyCondition paperPolicyCondition);

    /**
     * 条件查询 分页
     *
     * @param pageNum              页码，从1 开始
     * @param pageSize             每页多少条
     * @param paperPolicyCondition 查询条件
     * @return
     */
    PageInfo<PaperPolicyCondition> findByConditionWithPage(Integer pageNum, Integer pageSize, PaperPolicyCondition paperPolicyCondition);

    /**
     * 通过id查询
     *
     * @param paperPolicyConditionId
     * @return
     */
    PaperPolicyCondition findById(Long paperPolicyConditionId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param paperPolicyConditionId
     * @return
     */
    int deleteById(Long paperPolicyConditionId, Long operatorId);

    /**
     * 批量添加
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 10:47
     */
    List<PaperPolicyCondition> batchAdd(List<PaperPolicyCondition> conditionList);

    /**
     * 删除全部数据
     *
     * @param
     * @return
     * @auther daocers
     * @date 2019/5/7 11:09
     */
    int deleteAll();
}
