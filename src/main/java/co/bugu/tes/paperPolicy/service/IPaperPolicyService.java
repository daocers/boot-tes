package co.bugu.tes.paperPolicy.service;

import co.bugu.tes.paperPolicy.domain.PaperPolicy;
import com.github.pagehelper.PageInfo;
import java.util.List;

/**
* service接口
*
* @author daocers
* @create 2019-04-28 17:08
*/
public interface IPaperPolicyService {

    /**
    * 新增
    * @param paperPolicy
    * @return
    */
    long add(PaperPolicy paperPolicy);

    /**
    * 通过id更新
    * @param paperPolicy
    * @return
    */
    int updateById(PaperPolicy paperPolicy);

    /**
    * 条件查询
    * @param paperPolicy
    * @return
    */
    List<PaperPolicy> findByCondition(PaperPolicy paperPolicy);

    /**
    * 条件查询 分页
    * @param pageNum 页码，从1 开始
    * @param pageSize 每页多少条
    * @param paperPolicy 查询条件
    * @return
    */
    List<PaperPolicy> findByCondition(Integer pageNum, Integer pageSize, PaperPolicy paperPolicy);

    /**
    * 条件查询 分页
    * @param pageNum 页码，从1 开始
    * @param pageSize 每页多少条
    * @param paperPolicy 查询条件
    * @return
    */
    PageInfo<PaperPolicy> findByConditionWithPage(Integer pageNum, Integer pageSize, PaperPolicy paperPolicy);

    /**
    * 通过id查询
    * @param paperPolicyId
    * @return
    */
    PaperPolicy findById(Long paperPolicyId);

    /**
    * 删除指定id的记录 软删除，设置删除标志
    * @param paperPolicyId
    * @return
    */
    int deleteById(Long paperPolicyId, Long operatorId);

}
