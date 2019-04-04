package co.bugu.tes.receipt.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.receipt.dao.ReceiptDao;
import co.bugu.tes.receipt.domain.Receipt;
import co.bugu.tes.receipt.service.IReceiptService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author daocers
 * @create 2019-04-04 16:33
 */
@Service
public class ReceiptServiceImpl implements IReceiptService {
    @Autowired
    ReceiptDao receiptDao;

    private Logger logger = LoggerFactory.getLogger(ReceiptServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(Receipt receipt) {
        //todo 校验参数
        receipt.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        receipt.setCreateTime(now);
        receipt.setUpdateTime(now);
        receiptDao.insert(receipt);
        return receipt.getId();
    }

    @Override
    public int updateById(Receipt receipt) {
        logger.debug("receipt updateById, 参数： {}", JSON.toJSONString(receipt, true));
        Preconditions.checkNotNull(receipt.getId(), "id不能为空");
        receipt.setUpdateTime(new Date());
        return receiptDao.updateById(receipt);
    }

    @Override
    public List<Receipt> findByCondition(Receipt receipt) {
        logger.debug("receipt findByCondition, 参数： {}", JSON.toJSONString(receipt, true));
        PageHelper.orderBy(ORDER_BY);
        List<Receipt> receipts = receiptDao.findByObject(receipt);

        logger.debug("查询结果， {}", JSON.toJSONString(receipts, true));
        return receipts;
    }

    @Override
    public List<Receipt> findByCondition(Integer pageNum, Integer pageSize, Receipt receipt) {
        logger.debug("receipt findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(receipt, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Receipt> receipts = receiptDao.findByObject(receipt);

        logger.debug("查询结果， {}", JSON.toJSONString(receipts, true));
        return receipts;
    }

    @Override
    public PageInfo<Receipt> findByConditionWithPage(Integer pageNum, Integer pageSize, Receipt receipt) {
        logger.debug("receipt findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(receipt, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Receipt> receipts = receiptDao.findByObject(receipt);

        logger.debug("查询结果， {}", JSON.toJSONString(receipts, true));
        return new PageInfo<>(receipts);
    }

    @Override
    public Receipt findById(Long receiptId) {
        logger.debug("receipt findById, 参数 receiptId: {}", receiptId);
        Receipt receipt = receiptDao.selectById(receiptId);

        logger.debug("查询结果： {}", JSON.toJSONString(receipt, true));
        return receipt;
    }

    @Override
    public int deleteById(Long receiptId, Long operatorId) {
        logger.debug("receipt 删除， 参数 receiptId : {}", receiptId);
        Receipt receipt = new Receipt();
        receipt.setId(receiptId);
        receipt.setIsDel(DelFlagEnum.YES.getCode());
        receipt.setUpdateTime(new Date());
        receipt.setUpdateUserId(operatorId);
        int num = receiptDao.updateById(receipt);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

    @Override
    public List<Receipt> save(Long sceneId, Integer receiptCount, Integer numberLength) {
        Receipt receipt = new Receipt();
        receipt.setSceneId(sceneId);
        List<Receipt> list = receiptDao.findByObject(receipt);
        if (CollectionUtils.isNotEmpty(list)) {
            receiptDao.deleteBySceneId(sceneId);
        }

        double max = Math.pow(10, numberLength);
        Long maxNum = Math.round(max);
        Random random = new Random();
        int bound = maxNum.intValue();
        List<Receipt> receipts = new ArrayList<>();
        for (int i = 0; i < receiptCount; i++) {
            Integer num = random.nextInt(bound);
            Receipt item = new Receipt();
            item.setSceneId(sceneId);
            item.setNo(i);
            item.setNumber(num);
            item.setIsDel(1);
            item.setStatus(1);
            receipts.add(item);
        }
        receiptDao.batchAdd(receipts);
        return receipts;
    }

}
