package co.bugu.tes.receipt.api;

import co.bugu.common.RespDto;
import co.bugu.tes.receipt.domain.Receipt;
import co.bugu.tes.receipt.service.IReceiptService;
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

import java.util.List;

/**
 * 数据api
 *
 * @author daocers
 * @create 2019-04-04 16:33
 */
@RestController
@RequestMapping("/receipt/api")
public class ReceiptApi {
    private Logger logger = LoggerFactory.getLogger(ReceiptApi.class);

    @Autowired
    IReceiptService receiptService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2019-04-04 16:33
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Receipt>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Receipt receipt) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(receipt, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Receipt> list = receiptService.findByCondition(pageNum, pageSize, receipt);
            PageInfo<Receipt> pageInfo = new PageInfo<>(list);
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
     * @param receipt
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2019-04-04 16:33
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveReceipt(@RequestBody Receipt receipt) {
        try {
            User user  = UserUtil.getCurrentUser();
            Long receiptId = receipt.getId();
            receipt.setUpdateUserId(user.getId());
            if(null == receiptId){
                receipt.setCreateUserId(user.getId());
                logger.debug("保存， saveReceipt, 参数： {}", JSON.toJSONString(receipt, true));
                receiptId = receiptService.add(receipt);
                logger.info("新增 成功， id: {}", receiptId);
            }else{
                receiptService.updateById(receipt);
                logger.debug("更新成功", JSON.toJSONString(receipt, true));
            }
            return RespDto.success(receiptId != null);
        } catch (Exception e) {
            logger.error("保存 receipt 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.receipt.domain.Receipt>
     * @author daocers
     * @date 2019-04-04 16:33
     */
    @RequestMapping(value = "/findById")
    public RespDto<Receipt> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Receipt receipt = receiptService.findById(id);
            return RespDto.success(receipt);
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
     * @date 2019-04-04 16:33
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = receiptService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

