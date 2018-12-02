package co.bugu.tes.property.service;

import co.bugu.tes.property.domain.Property;
import co.bugu.tes.propertyItem.domain.PropertyItem;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * service接口
 *
 * @author daocers
 * @create 2018-12-02 11:40
 */
public interface IPropertyService {

    /**
     * 新增
     *
     * @param property
     * @return
     */
    long add(Property property, List<PropertyItem> itemList);

    /**
     * 通过id更新
     *
     * @param property
     * @return
     */
    int updateById(Property property, List<PropertyItem> itemList);

    /**
     * 条件查询
     *
     * @param property
     * @return
     */
    List<Property> findByCondition(Property property);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param property 查询条件
     * @return
     */
    List<Property> findByCondition(Integer pageNum, Integer pageSize, Property property);

    /**
     * 条件查询 分页
     *
     * @param pageNum  页码，从1 开始
     * @param pageSize 每页多少条
     * @param property 查询条件
     * @return
     */
    PageInfo<Property> findByConditionWithPage(Integer pageNum, Integer pageSize, Property property);

    /**
     * 通过id查询
     *
     * @param propertyId
     * @return
     */
    Property findById(Long propertyId);

    /**
     * 删除指定id的记录 软删除，设置删除标志
     *
     * @param propertyId
     * @return
     */
    int deleteById(Long propertyId, Long operatorId);

}
