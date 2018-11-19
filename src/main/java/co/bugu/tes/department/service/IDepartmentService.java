package co.bugu.tes.department.service;

import co.bugu.tes.department.domain.Department;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-19 17:51
 */
public interface IDepartmentService {

    /**
     * 新增
     *
     * @param department
     * @return
     */
    long add(Department department);

    /**
     * 通过id更新
     *
     * @param department
     * @return
     */
    int updateById(Department department);

    /**
     * 条件查询
     *
     * @param department
     * @return
     */
    List<Department> findByCondition(Department department);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param department     查询条件
     * @return
     */
    List<Department> findByCondition(Integer pageNum, Integer pageSize, Department department);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param department     查询条件
     * @return
     */
    PageInfo<Department> findByConditionWithPage(Integer pageNum, Integer pageSize, Department department);

    /**
     * 通过id查询
     *
     * @param departmentId
     * @return
     */
    Department findById(Long departmentId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param departmentId
     * @return
     */
    int deleteById(Long departmentId, Long operatorId);

}
