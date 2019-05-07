package co.bugu.tes.paperPolicyCondition.api;

import co.bugu.common.RespDto;
import co.bugu.tes.paperPolicyCondition.domain.PaperPolicyCondition;
import co.bugu.tes.paperPolicyCondition.service.IPaperPolicyConditionService;
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
 * @create 2019-05-07 10:25
 */
@RestController
@RequestMapping("/paperPolicyCondition/api")
public class PaperPolicyConditionApi {
    private static Logger logger = LoggerFactory.getLogger(PaperPolicyConditionApi.class);

    @Autowired
    IPaperPolicyConditionService paperPolicyConditionService;

    /**
     * 条件查询
     * @param
     * @return
     * @author daocers
     * @date 2019-05-07 10:25
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<PaperPolicyCondition>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody PaperPolicyCondition paperPolicyCondition) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(paperPolicyCondition, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<PaperPolicyCondition> list = paperPolicyConditionService.findByCondition(pageNum, pageSize, paperPolicyCondition);
            PageInfo<PaperPolicyCondition> pageInfo = new PageInfo<>(list);
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
     * @param paperPolicyCondition
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2019-05-07 10:25
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> savePaperPolicyCondition(@RequestBody PaperPolicyCondition paperPolicyCondition) {
        try {
            logger.debug("保存， savePaperPolicyCondition, 参数： {}", JSON.toJSONString(paperPolicyCondition, true));
            Long paperPolicyConditionId = paperPolicyConditionService.add(paperPolicyCondition);
            logger.info("新增 成功， id: {}", paperPolicyConditionId);

            return RespDto.success(paperPolicyConditionId != null);
        } catch (Exception e) {
            logger.error("保存 paperPolicyCondition 失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 更新指定id的记录
     *
     * @param paperPolicyCondition
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2019-05-07 10:25
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RespDto<Boolean> updateById(@RequestBody PaperPolicyCondition paperPolicyCondition) {
        try {
            logger.debug("更新，updateById, 参数: {}", JSON.toJSONString(paperPolicyCondition, true));
            Preconditions.checkArgument(paperPolicyCondition != null, "paperPolicyCondition不能为空");
            Preconditions.checkArgument(null != paperPolicyCondition.getId(), "id不能为空");
            int res = paperPolicyConditionService.updateById(paperPolicyCondition);

            return RespDto.success(res == 1);

        } catch (Exception e) {
            logger.error("更新 paperPolicyCondition 失败", e);
            return RespDto.fail();
        }
     }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.paperPolicyCondition.domain.PaperPolicyCondition>
     * @author daocers
     * @date 2019-05-07 10:25
     */
    @RequestMapping(value = "/getDetail")
    public RespDto<PaperPolicyCondition> getDetail(Long id) {
        try {
            logger.info("获取详情，getDetail, id： {}", id);
            PaperPolicyCondition paperPolicyCondition = paperPolicyConditionService.findById(id);
            return RespDto.success(paperPolicyCondition);
        } catch (Exception e) {
            logger.error("获取详情失败", e);
            return RespDto.fail();
        }
    }

    /**
     *       删除，软删除，更新数据库删除标志
     * @param
     * @return
     * @author daocers
     * @date 2019-05-07 10:25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = paperPolicyConditionService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

