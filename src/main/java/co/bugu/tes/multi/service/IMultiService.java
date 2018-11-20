package co.bugu.tes.multi.service;

import co.bugu.tes.multi.domain.Multi;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-11-20 17:15
 */
public interface IMultiService {

    /**
     * 新增
     *
     * @param multi
     * @return
     */
    long add(Multi multi);

    /**
     * 通过id更新
     *
     * @param multi
     * @return
     */
    int updateById(Multi multi);

    /**
     * 条件查询
     *
     * @param multi
     * @return
     */
    List<Multi> findByCondition(Multi multi);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param multi     查询条件
     * @return
     */
    List<Multi> findByCondition(Integer pageNum, Integer pageSize, Multi multi);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param multi     查询条件
     * @return
     */
    PageInfo<Multi> findByConditionWithPage(Integer pageNum, Integer pageSize, Multi multi);

    /**
     * 通过id查询
     *
     * @param multiId
     * @return
     */
    Multi findById(Long multiId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param multiId
     * @return
     */
    int deleteById(Long multiId, Long operatorId);

}
