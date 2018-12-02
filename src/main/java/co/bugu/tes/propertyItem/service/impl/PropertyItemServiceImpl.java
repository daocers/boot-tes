package co.bugu.tes.propertyItem.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.propertyItem.dao.PropertyItemDao;
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

import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-12-02 11:40
 */
@Service
public class PropertyItemServiceImpl implements IPropertyItemService {
    @Autowired
    PropertyItemDao propertyItemDao;

    private Logger logger = LoggerFactory.getLogger(PropertyItemServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(PropertyItem propertyItem) {
        //todo 校验参数
        propertyItem.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        propertyItem.setCreateTime(now);
        propertyItem.setUpdateTime(now);
        propertyItemDao.insert(propertyItem);
        return propertyItem.getId();
    }

    @Override
    public int updateById(PropertyItem propertyItem) {
        logger.debug("propertyItem updateById, 参数： {}", JSON.toJSONString(propertyItem, true));
        Preconditions.checkNotNull(propertyItem.getId(), "id不能为空");
        propertyItem.setUpdateTime(new Date());
        return propertyItemDao.updateById(propertyItem);
    }

    @Override
    public List<PropertyItem> findByCondition(PropertyItem propertyItem) {
        logger.debug("propertyItem findByCondition, 参数： {}", JSON.toJSONString(propertyItem, true));
        PageHelper.orderBy(ORDER_BY);
        List<PropertyItem> propertyItems = propertyItemDao.findByObject(propertyItem);

        logger.debug("查询结果， {}", JSON.toJSONString(propertyItems, true));
        return propertyItems;
    }

    @Override
    public List<PropertyItem> findByCondition(Integer pageNum, Integer pageSize, PropertyItem propertyItem) {
        logger.debug("propertyItem findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(propertyItem, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<PropertyItem> propertyItems = propertyItemDao.findByObject(propertyItem);

        logger.debug("查询结果， {}", JSON.toJSONString(propertyItems, true));
        return propertyItems;
    }

    @Override
    public PageInfo<PropertyItem> findByConditionWithPage(Integer pageNum, Integer pageSize, PropertyItem propertyItem) {
        logger.debug("propertyItem findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(propertyItem, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<PropertyItem> propertyItems = propertyItemDao.findByObject(propertyItem);

        logger.debug("查询结果， {}", JSON.toJSONString(propertyItems, true));
        return new PageInfo<>(propertyItems);
    }

    @Override
    public PropertyItem findById(Long propertyItemId) {
        logger.debug("propertyItem findById, 参数 propertyItemId: {}", propertyItemId);
        PropertyItem propertyItem = propertyItemDao.selectById(propertyItemId);

        logger.debug("查询结果： {}", JSON.toJSONString(propertyItem, true));
        return propertyItem;
    }

    @Override
    public int deleteById(Long propertyItemId, Long operatorId) {
        logger.debug("propertyItem 删除， 参数 propertyItemId : {}", propertyItemId);
        PropertyItem propertyItem = new PropertyItem();
        propertyItem.setId(propertyItemId);
        propertyItem.setIsDel(DelFlagEnum.YES.getCode());
        propertyItem.setUpdateTime(new Date());
        propertyItem.setUpdateUserId(operatorId);
        int num = propertyItemDao.updateById(propertyItem);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
