package co.bugu.tes.receiptAnswer.api;

import co.bugu.common.RespDto;
import co.bugu.tes.receipt.service.IReceiptService;
import co.bugu.tes.receiptAnswer.agent.ReceiptAnswerAgent;
import co.bugu.tes.receiptAnswer.domain.ReceiptAnswer;
import co.bugu.tes.receiptAnswer.service.IReceiptAnswerService;
import co.bugu.tes.scene.service.ISceneService;
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
@RequestMapping("/receiptAnswer/api")
public class ReceiptAnswerApi {
    private Logger logger = LoggerFactory.getLogger(ReceiptAnswerApi.class);

    @Autowired
    IReceiptAnswerService receiptAnswerService;
    @Autowired
    IReceiptService receiptService;

    @Autowired
    ReceiptAnswerAgent receiptAnswerAgent;

    @Autowired
    ISceneService sceneService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2019-04-04 16:33
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<ReceiptAnswer>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody ReceiptAnswer receiptAnswer) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(receiptAnswer, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<ReceiptAnswer> list = receiptAnswerService.findByCondition(pageNum, pageSize, receiptAnswer);
            PageInfo<ReceiptAnswer> pageInfo = new PageInfo<>(list);
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
     * @param receiptAnswer
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2019-04-04 16:33
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveReceiptAnswer(@RequestBody ReceiptAnswer receiptAnswer) {
        try {
            Long receiptAnswerId = receiptAnswer.getId();
            if (null == receiptAnswerId) {
                logger.debug("保存， saveReceiptAnswer, 参数： {}", JSON.toJSONString(receiptAnswer, true));
                receiptAnswerId = receiptAnswerService.add(receiptAnswer);
                logger.info("新增 成功， id: {}", receiptAnswerId);
            } else {
                receiptAnswerService.updateById(receiptAnswer);
                logger.debug("更新成功", JSON.toJSONString(receiptAnswer, true));
            }
            return RespDto.success(receiptAnswerId != null);
        } catch (Exception e) {
            logger.error("保存 receiptAnswer 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.receiptAnswer.domain.ReceiptAnswer>
     * @author daocers
     * @date 2019-04-04 16:33
     */
    @RequestMapping(value = "/findById")
    public RespDto<ReceiptAnswer> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            ReceiptAnswer receiptAnswer = receiptAnswerService.findById(id);
            return RespDto.success(receiptAnswer);
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
            int count = receiptAnswerService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

