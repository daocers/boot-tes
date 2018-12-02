package co.bugu.tes.property.service.impl;

import co.bugu.common.enums.BaseStatusEnum;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.property.dao.PropertyDao;
import co.bugu.tes.property.domain.Property;
import co.bugu.tes.property.service.IPropertyService;
import co.bugu.tes.propertyItem.domain.PropertyItem;
import co.bugu.tes.propertyItem.service.IPropertyItemService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-12-02 11:40
 */
@Service
public class PropertyServiceImpl implements IPropertyService {
    @Autowired
    PropertyDao propertyDao;
    @Autowired
    IPropertyItemService propertyItemService;

    private Logger logger = LoggerFactory.getLogger(PropertyServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    @Transactional(timeout = 3000, rollbackFor = Exception.class)
    public long add(Property property, List<PropertyItem> itemList) {
        //todo 校验参数
        property.setIsDel(DelFlagEnum.NO.getCode());
        Date now = new Date();
        property.setCreateTime(now);
        property.setUpdateTime(now);
        propertyDao.insert(property);
        Long propertyId = property.getId();
        for (PropertyItem item : itemList) {
            item.setPropertyId(propertyId);
            item.setIsDel(DelFlagEnum.NO.getCode());
            item.setStatus(BaseStatusEnum.ENABLE.getCode());
            propertyItemService.add(item);
        }
        return propertyId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 3000)
    public int updateById(Property property, List<PropertyItem> itemList) {
        logger.debug("property updateById, 参数： {}", JSON.toJSONString(property, true));
        Preconditions.checkNotNull(property.getId(), "id不能为空");
        property.setUpdateTime(new Date());
        Long propertyId = property.getId();
        propertyDao.updateById(property);

        for (PropertyItem item : itemList) {
            item.setPropertyId(propertyId);
            item.setStatus(BaseStatusEnum.ENABLE.getCode());
            item.setIsDel(DelFlagEnum.NO.getCode());
            item.setUpdateUserId(property.getUpdateUserId());
            Long id = item.getId();
            if (null == id) {
                item.setCreateUserId(property.getUpdateUserId());
                propertyItemService.add(item);
            } else {
                propertyItemService.updateById(item);
            }
        }
        return 1;
    }

    @Override
    public List<Property> findByCondition(Property property) {
        logger.debug("property findByCondition, 参数： {}", JSON.toJSONString(property, true));
        PageHelper.orderBy(ORDER_BY);
        List<Property> propertys = propertyDao.findByObject(property);

        logger.debug("查询结果， {}", JSON.toJSONString(propertys, true));
        return propertys;
    }

    @Override
    public List<Property> findByCondition(Integer pageNum, Integer pageSize, Property property) {
        logger.debug("property findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(property, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Property> propertys = propertyDao.findByObject(property);

        logger.debug("查询结果， {}", JSON.toJSONString(propertys, true));
        return propertys;
    }

    @Override
    public PageInfo<Property> findByConditionWithPage(Integer pageNum, Integer pageSize, Property property) {
        logger.debug("property findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(property, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Property> propertys = propertyDao.findByObject(property);

        logger.debug("查询结果， {}", JSON.toJSONString(propertys, true));
        return new PageInfo<>(propertys);
    }

    @Override
    public Property findById(Long propertyId) {
        logger.debug("property findById, 参数 propertyId: {}", propertyId);
        Property property = propertyDao.selectById(propertyId);

        logger.debug("查询结果： {}", JSON.toJSONString(property, true));
        return property;
    }

    @Override
    public int deleteById(Long propertyId, Long operatorId) {
        logger.debug("property 删除， 参数 propertyId : {}", propertyId);
        Property property = new Property();
        property.setId(propertyId);
        property.setIsDel(DelFlagEnum.YES.getCode());
        property.setUpdateTime(new Date());
        property.setUpdateUserId(operatorId);
        int num = propertyDao.updateById(property);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
