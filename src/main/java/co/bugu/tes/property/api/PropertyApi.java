package co.bugu.tes.property.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.property.domain.Property;
import co.bugu.tes.property.dto.PropertyDto;
import co.bugu.tes.property.service.IPropertyService;
import co.bugu.tes.propertyItem.domain.PropertyItem;
import co.bugu.tes.propertyItem.service.IPropertyItemService;
import co.bugu.tes.user.domain.User;
import co.bugu.tes.user.service.IUserService;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-12-02 11:40
 */
@RestController
@RequestMapping("/property/api")
public class PropertyApi {
    private Logger logger = LoggerFactory.getLogger(PropertyApi.class);

    @Autowired
    IPropertyService propertyService;
    @Autowired
    IUserService userService;
    @Autowired
    IPropertyItemService propertyItemService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-12-02 11:40
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<PropertyDto>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Property property) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(property, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            PageInfo<Property> pageInfo = propertyService.findByConditionWithPage(pageNum, pageSize, property);
            PageInfo<PropertyDto> res = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, res);
            List<PropertyDto> list = Lists.transform(pageInfo.getList(), new Function<Property, PropertyDto>() {
                @Override
                public PropertyDto apply(@Nullable Property property) {
                    PropertyDto dto = new PropertyDto();
                    BeanUtils.copyProperties(property, dto);
                    User user = userService.findById(property.getCreateUserId());
                    if (null != user) {
                        dto.setCreateUserName(user.getName());
                    }
                    PropertyItem query = new PropertyItem();
                    query.setPropertyId(property.getId());
                    query.setIsDel(DelFlagEnum.NO.getCode());
                    List<PropertyItem> itemList = propertyItemService.findByCondition(query);
                    dto.setItemList(itemList);
                    return dto;
                }
            });
            res.setList(list);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(res);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 保存
     *
     * @param property
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-12-02 11:40
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveProperty(@RequestBody PropertyDto propertyDto) {
        try {
            Property property = new Property();
            BeanUtils.copyProperties(propertyDto, property);
            Long userId = UserUtil.getCurrentUser().getId();
            property.setUpdateUserId(userId);
            Long propertyId = property.getId();
            if (null == propertyId) {
                property.setCreateUserId(userId);
                logger.debug("保存， saveProperty, 参数： {}", JSON.toJSONString(property, true));
                propertyId = propertyService.add(property, propertyDto.getItemList());
                logger.info("新增 成功， id: {}", propertyId);
            } else {
                propertyService.updateById(property, propertyDto.getItemList());
                logger.debug("更新成功", JSON.toJSONString(property, true));
            }
            return RespDto.success(propertyId != null);
        } catch (Exception e) {
            logger.error("保存 property 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.property.domain.Property>
     * @author daocers
     * @date 2018-12-02 11:40
     */
    @RequestMapping(value = "/findById")
    public RespDto<Property> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Property property = propertyService.findById(id);
            return RespDto.success(property);
        } catch (Exception e) {
            logger.error("获取详情失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 删除，软删除，更新数据库删除标志
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-12-02 11:40
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = propertyService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

