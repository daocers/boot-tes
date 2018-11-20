package co.bugu.tes.multi.api;

import co.bugu.common.RespDto;
import co.bugu.tes.multi.domain.Multi;
import co.bugu.tes.multi.service.IMultiService;
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
 * @create 2018-11-20 17:15
 */
@RestController
@RequestMapping("/multi/api")
public class MultiApi {
    private Logger logger = LoggerFactory.getLogger(MultiApi.class);

    @Autowired
    IMultiService multiService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Multi>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Multi multi) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(multi, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Multi> list = multiService.findByCondition(pageNum, pageSize, multi);
            PageInfo<Multi> pageInfo = new PageInfo<>(list);
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
     * @param multi
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveMulti(@RequestBody Multi multi) {
        try {
            Long multiId = multi.getId();
            if(null == multiId){
                logger.debug("保存， saveMulti, 参数： {}", JSON.toJSONString(multi, true));
                multiId = multiService.add(multi);
                logger.info("新增 成功， id: {}", multiId);
            }else{
                multiService.updateById(multi);
                logger.debug("更新成功", JSON.toJSONString(multi, true));
            }
            return RespDto.success(multiId != null);
        } catch (Exception e) {
            logger.error("保存 multi 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.multi.domain.Multi>
     * @author daocers
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/findById")
    public RespDto<Multi> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Multi multi = multiService.findById(id);
            return RespDto.success(multi);
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
     * @date 2018-11-20 17:15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = multiService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

