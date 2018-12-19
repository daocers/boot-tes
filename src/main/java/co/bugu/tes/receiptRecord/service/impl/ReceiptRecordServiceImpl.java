package co.bugu.tes.receiptRecord.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.receiptRecord.dao.ReceiptRecordDao;
import co.bugu.tes.receiptRecord.domain.ReceiptRecord;
import co.bugu.tes.receiptRecord.service.IReceiptRecordService;
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
 * @create 2018-12-19 09:56
 */
@Service
public class ReceiptRecordServiceImpl implements IReceiptRecordService {
    @Autowired
    ReceiptRecordDao receiptRecordDao;

    private Logger logger = LoggerFactory.getLogger(ReceiptRecordServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(ReceiptRecord receiptRecord) {
        //todo 校验参数
        receiptRecord.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        receiptRecord.setCreateTime(now);
        receiptRecord.setUpdateTime(now);
        receiptRecordDao.insert(receiptRecord);
        return receiptRecord.getId();
    }

    @Override
    public int updateById(ReceiptRecord receiptRecord) {
        logger.debug("receiptRecord updateById, 参数： {}", JSON.toJSONString(receiptRecord, true));
        Preconditions.checkNotNull(receiptRecord.getId(), "id不能为空");
        receiptRecord.setUpdateTime(new Date());
        return receiptRecordDao.updateById(receiptRecord);
    }

    @Override
    public List<ReceiptRecord> findByCondition(ReceiptRecord receiptRecord) {
        logger.debug("receiptRecord findByCondition, 参数： {}", JSON.toJSONString(receiptRecord, true));
        PageHelper.orderBy(ORDER_BY);
        List<ReceiptRecord> receiptRecords = receiptRecordDao.findByObject(receiptRecord);

        logger.debug("查询结果， {}", JSON.toJSONString(receiptRecords, true));
        return receiptRecords;
    }

    @Override
    public List<ReceiptRecord> findByCondition(Integer pageNum, Integer pageSize, ReceiptRecord receiptRecord) {
        logger.debug("receiptRecord findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(receiptRecord, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<ReceiptRecord> receiptRecords = receiptRecordDao.findByObject(receiptRecord);

        logger.debug("查询结果， {}", JSON.toJSONString(receiptRecords, true));
        return receiptRecords;
    }

    @Override
    public PageInfo<ReceiptRecord> findByConditionWithPage(Integer pageNum, Integer pageSize, ReceiptRecord receiptRecord) {
        logger.debug("receiptRecord findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(receiptRecord, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<ReceiptRecord> receiptRecords = receiptRecordDao.findByObject(receiptRecord);

        logger.debug("查询结果， {}", JSON.toJSONString(receiptRecords, true));
        return new PageInfo<>(receiptRecords);
    }

    @Override
    public ReceiptRecord findById(Long receiptRecordId) {
        logger.debug("receiptRecord findById, 参数 receiptRecordId: {}", receiptRecordId);
        ReceiptRecord receiptRecord = receiptRecordDao.selectById(receiptRecordId);

        logger.debug("查询结果： {}", JSON.toJSONString(receiptRecord, true));
        return receiptRecord;
    }

    @Override
    public int deleteById(Long receiptRecordId, Long operatorId) {
        logger.debug("receiptRecord 删除， 参数 receiptRecordId : {}", receiptRecordId);
        ReceiptRecord receiptRecord = new ReceiptRecord();
        receiptRecord.setId(receiptRecordId);
        receiptRecord.setIsDel(DelFlagEnum.YES.getCode());
        receiptRecord.setUpdateTime(new Date());
        receiptRecord.setUpdateUserId(operatorId);
        int num = receiptRecordDao.updateById(receiptRecord);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
