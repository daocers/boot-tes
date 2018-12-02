package co.bugu.tes.propertyItem.api;

import co.bugu.common.RespDto;
import co.bugu.tes.propertyItem.domain.PropertyItem;
import co.bugu.tes.propertyItem.service.IPropertyItemService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/propertyItem/api")
public class PropertyItemApi {
    private Logger logger = LoggerFactory.getLogger(PropertyItemApi.class);

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
    public RespDto<PageInfo<PropertyItem>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody PropertyItem propertyItem) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(propertyItem, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<PropertyItem> list = propertyItemService.findByCondition(pageNum, pageSize, propertyItem);
            PageInfo<PropertyItem> pageInfo = new PageInfo<>(list);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(pageInfo);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 保存
     *
     * @param propertyItem
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-12-02 11:40
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> savePropertyItem(@RequestBody PropertyItem propertyItem) {
        try {
            Long propertyItemId = propertyItem.getId();
            if(null == propertyItemId){
                logger.debug("保存， savePropertyItem, 参数： {}", JSON.toJSONString(propertyItem, true));
                propertyItemId = propertyItemService.add(propertyItem);
                logger.info("新增 成功， id: {}", propertyItemId);
            }else{
                propertyItemService.updateById(propertyItem);
                logger.debug("更新成功", JSON.toJSONString(propertyItem, true));
            }
            return RespDto.success(propertyItemId != null);
        } catch (Exception e) {
            logger.error("保存 propertyItem 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.propertyItem.domain.PropertyItem>
     * @author daocers
     * @date 2018-12-02 11:40
     */
    @RequestMapping(value = "/findById")
    public RespDto<PropertyItem> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            PropertyItem propertyItem = propertyItemService.findById(id);
            return RespDto.success(propertyItem);
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
            int count = propertyItemService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

