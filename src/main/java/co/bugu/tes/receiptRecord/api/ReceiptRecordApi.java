package co.bugu.tes.receiptRecord.api;

import co.bugu.common.RespDto;
import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.receiptRecord.domain.ReceiptRecord;
import co.bugu.tes.receiptRecord.service.IReceiptRecordService;
import co.bugu.tes.user.domain.User;
import co.bugu.util.UserUtil;
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

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-12-19 09:56
 */
@RestController
@RequestMapping("/receiptRecord/api")
public class ReceiptRecordApi {
    private Logger logger = LoggerFactory.getLogger(ReceiptRecordApi.class);

    @Autowired
    IReceiptRecordService receiptRecordService;

    @RequestMapping(value = "/getMyRecords")
    public RespDto<PageInfo<ReceiptRecord>> getMyRecords(Integer pageNum, Integer pageSize) {
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }

            Long userId = UserUtil.getCurrentUser().getId();
            ReceiptRecord receiptRecord = new ReceiptRecord();
            receiptRecord.setCreateUserId(userId);
            PageInfo<ReceiptRecord> pageInfo = receiptRecordService.findByConditionWithPage(pageNum, pageSize, receiptRecord);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(pageInfo);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-12-19 09:56
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<ReceiptRecord>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody ReceiptRecord receiptRecord) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(receiptRecord, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            User user = UserUtil.getCurrentUser();
            receiptRecord.setUserId(user.getId());
            PageInfo<ReceiptRecord> pageInfo = receiptRecordService.findByConditionWithPage(pageNum, pageSize, receiptRecord);
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
     * @param receiptRecord
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-12-19 09:56
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveReceiptRecord(@RequestBody ReceiptRecord receiptRecord) {
        try {
            Long receiptRecordId = receiptRecord.getId();
            Long userId = UserUtil.getCurrentUser().getId();
            receiptRecord.setUpdateUserId(userId);
            receiptRecord.setUserId(userId);
            receiptRecord.setCount(receiptRecord.getFalseCount() + receiptRecord.getRightCount());
            if (null == receiptRecordId) {
                logger.debug("保存， saveReceiptRecord, 参数： {}", JSON.toJSONString(receiptRecord, true));
                receiptRecord.setIsDel(DelFlagEnum.NO.getCode());
                receiptRecord.setCreateUserId(userId);
                receiptRecordId = receiptRecordService.add(receiptRecord);
                logger.info("新增 成功， id: {}", receiptRecordId);
            } else {
//                原则上无法到达这里
                logger.warn("理论不可达的请求，请查看：{}", JSON.toJSONString(receiptRecord, true));
                receiptRecordService.updateById(receiptRecord);
                logger.debug("更新成功", JSON.toJSONString(receiptRecord, true));
            }
            return RespDto.success(receiptRecordId != null);
        } catch (Exception e) {
            logger.error("保存 receiptRecord 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.receiptRecord.domain.ReceiptRecord>
     * @author daocers
     * @date 2018-12-19 09:56
     */
    @RequestMapping(value = "/findById")
    public RespDto<ReceiptRecord> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            ReceiptRecord receiptRecord = receiptRecordService.findById(id);
            return RespDto.success(receiptRecord);
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
     * @date 2018-12-19 09:56
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = receiptRecordService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

