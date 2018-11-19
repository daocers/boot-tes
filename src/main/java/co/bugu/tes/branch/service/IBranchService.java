package co.bugu.tes.branch.service;

import co.bugu.tes.branch.domain.Branch;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-19 19:29
 */
public interface IBranchService {

    /**
     * 新增
     *
     * @param branch
     * @return
     */
    long add(Branch branch);

    /**
     * 通过id更新
     *
     * @param branch
     * @return
     */
    int updateById(Branch branch);

    /**
     * 条件查询
     *
     * @param branch
     * @return
     */
    List<Branch> findByCondition(Branch branch);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param branch     查询条件
     * @return
     */
    List<Branch> findByCondition(Integer pageNum, Integer pageSize, Branch branch);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param branch     查询条件
     * @return
     */
    PageInfo<Branch> findByConditionWithPage(Integer pageNum, Integer pageSize, Branch branch);

    /**
     * 通过id查询
     *
     * @param branchId
     * @return
     */
    Branch findById(Long branchId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param branchId
     * @return
     */
    int deleteById(Long branchId, Long operatorId);

}
