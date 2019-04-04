package co.bugu.tes.receiptAnswer.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.receiptAnswer.dao.ReceiptAnswerDao;
import co.bugu.tes.receiptAnswer.domain.ReceiptAnswer;
import co.bugu.tes.receiptAnswer.service.IReceiptAnswerService;
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
 * @create 2019-04-04 16:33
 */
@Service
public class ReceiptAnswerServiceImpl implements IReceiptAnswerService {
    @Autowired
    ReceiptAnswerDao receiptAnswerDao;

    private Logger logger = LoggerFactory.getLogger(ReceiptAnswerServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(ReceiptAnswer receiptAnswer) {
        //todo 校验参数
        receiptAnswer.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        receiptAnswer.setCreateTime(now);
        receiptAnswer.setUpdateTime(now);
        receiptAnswerDao.insert(receiptAnswer);
        return receiptAnswer.getId();
    }

    @Override
    public int updateById(ReceiptAnswer receiptAnswer) {
        logger.debug("receiptAnswer updateById, 参数： {}", JSON.toJSONString(receiptAnswer, true));
        Preconditions.checkNotNull(receiptAnswer.getId(), "id不能为空");
        receiptAnswer.setUpdateTime(new Date());
        return receiptAnswerDao.updateById(receiptAnswer);
    }

    @Override
    public List<ReceiptAnswer> findByCondition(ReceiptAnswer receiptAnswer) {
        logger.debug("receiptAnswer findByCondition, 参数： {}", JSON.toJSONString(receiptAnswer, true));
        PageHelper.orderBy(ORDER_BY);
        List<ReceiptAnswer> receiptAnswers = receiptAnswerDao.findByObject(receiptAnswer);

        logger.debug("查询结果， {}", JSON.toJSONString(receiptAnswers, true));
        return receiptAnswers;
    }

    @Override
    public List<ReceiptAnswer> findByCondition(Integer pageNum, Integer pageSize, ReceiptAnswer receiptAnswer) {
        logger.debug("receiptAnswer findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(receiptAnswer, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<ReceiptAnswer> receiptAnswers = receiptAnswerDao.findByObject(receiptAnswer);

        logger.debug("查询结果， {}", JSON.toJSONString(receiptAnswers, true));
        return receiptAnswers;
    }

    @Override
    public PageInfo<ReceiptAnswer> findByConditionWithPage(Integer pageNum, Integer pageSize, ReceiptAnswer receiptAnswer) {
        logger.debug("receiptAnswer findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(receiptAnswer, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<ReceiptAnswer> receiptAnswers = receiptAnswerDao.findByObject(receiptAnswer);

        logger.debug("查询结果， {}", JSON.toJSONString(receiptAnswers, true));
        return new PageInfo<>(receiptAnswers);
    }

    @Override
    public ReceiptAnswer findById(Long receiptAnswerId) {
        logger.debug("receiptAnswer findById, 参数 receiptAnswerId: {}", receiptAnswerId);
        ReceiptAnswer receiptAnswer = receiptAnswerDao.selectById(receiptAnswerId);

        logger.debug("查询结果： {}", JSON.toJSONString(receiptAnswer, true));
        return receiptAnswer;
    }

    @Override
    public int deleteById(Long receiptAnswerId, Long operatorId) {
        logger.debug("receiptAnswer 删除， 参数 receiptAnswerId : {}", receiptAnswerId);
        ReceiptAnswer receiptAnswer = new ReceiptAnswer();
        receiptAnswer.setId(receiptAnswerId);
        receiptAnswer.setIsDel(DelFlagEnum.YES.getCode());
        receiptAnswer.setUpdateTime(new Date());
        receiptAnswer.setUpdateUserId(operatorId);
        int num = receiptAnswerDao.updateById(receiptAnswer);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
