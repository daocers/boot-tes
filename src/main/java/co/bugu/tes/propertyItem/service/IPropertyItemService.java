package co.bugu.tes.propertyItem.service;

import co.bugu.tes.propertyItem.domain.PropertyItem;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-12-02 11:40
 */
public interface IPropertyItemService {

    /**
     * 新增
     *
     * @param propertyItem
     * @return
     */
    long add(PropertyItem propertyItem);

    /**
     * 通过id更新
     *
     * @param propertyItem
     * @return
     */
    int updateById(PropertyItem propertyItem);

    /**
     * 条件查询
     *
     * @param propertyItem
     * @return
     */
    List<PropertyItem> findByCondition(PropertyItem propertyItem);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param propertyItem     查询条件
     * @return
     */
    List<PropertyItem> findByCondition(Integer pageNum, Integer pageSize, PropertyItem propertyItem);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param propertyItem     查询条件
     * @return
     */
    PageInfo<PropertyItem> findByConditionWithPage(Integer pageNum, Integer pageSize, PropertyItem propertyItem);

    /**
     * 通过id查询
     *
     * @param propertyItemId
     * @return
     */
    PropertyItem findById(Long propertyItemId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param propertyItemId
     * @return
     */
    int deleteById(Long propertyItemId, Long operatorId);

}
