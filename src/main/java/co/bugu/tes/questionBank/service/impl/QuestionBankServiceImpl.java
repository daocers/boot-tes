package co.bugu.tes.questionBank.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.questionBank.dao.QuestionBankDao;
import co.bugu.tes.questionBank.domain.QuestionBank;
import co.bugu.tes.questionBank.service.IQuestionBankService;
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
 * @create 2018-11-19 17:52
 */
@Service
public class QuestionBankServiceImpl implements IQuestionBankService {
    @Autowired
    QuestionBankDao questionBankDao;

    private Logger logger = LoggerFactory.getLogger(QuestionBankServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(QuestionBank questionBank) {
        //todo 校验参数
        questionBank.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        questionBank.setCreateTime(now);
        questionBank.setUpdateTime(now);
        questionBankDao.insert(questionBank);
        return questionBank.getId();
    }

    @Override
    public int updateById(QuestionBank questionBank) {
        logger.debug("questionBank updateById, 参数： {}", JSON.toJSONString(questionBank, true));
        Preconditions.checkNotNull(questionBank.getId(), "id不能为空");
        questionBank.setUpdateTime(new Date());
        return questionBankDao.updateById(questionBank);
    }

    @Override
    public List<QuestionBank> findByCondition(QuestionBank questionBank) {
        logger.debug("questionBank findByCondition, 参数： {}", JSON.toJSONString(questionBank, true));
        PageHelper.orderBy(ORDER_BY);
        List<QuestionBank> questionBanks = questionBankDao.findByObject(questionBank);

        logger.debug("查询结果， {}", JSON.toJSONString(questionBanks, true));
        return questionBanks;
    }

    @Override
    public List<QuestionBank> findByCondition(Integer pageNum, Integer pageSize, QuestionBank questionBank) {
        logger.debug("questionBank findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(questionBank, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<QuestionBank> questionBanks = questionBankDao.findByObject(questionBank);

        logger.debug("查询结果， {}", JSON.toJSONString(questionBanks, true));
        return questionBanks;
    }

    @Override
    public PageInfo<QuestionBank> findByConditionWithPage(Integer pageNum, Integer pageSize, QuestionBank questionBank) {
        logger.debug("questionBank findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(questionBank, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<QuestionBank> questionBanks = questionBankDao.findByObject(questionBank);

        logger.debug("查询结果， {}", JSON.toJSONString(questionBanks, true));
        return new PageInfo<>(questionBanks);
    }

    @Override
    public QuestionBank findById(Long questionBankId) {
        logger.debug("questionBank findById, 参数 questionBankId: {}", questionBankId);
        QuestionBank questionBank = questionBankDao.selectById(questionBankId);

        logger.debug("查询结果： {}", JSON.toJSONString(questionBank, true));
        return questionBank;
    }

    @Override
    public int deleteById(Long questionBankId, Long operatorId) {
        logger.debug("questionBank 删除， 参数 questionBankId : {}", questionBankId);
        QuestionBank questionBank = new QuestionBank();
        questionBank.setId(questionBankId);
        questionBank.setIsDel(DelFlagEnum.YES.getCode());
        questionBank.setUpdateTime(new Date());
        questionBank.setUpdateQuestionBankId(operatorId);
        int num = questionBankDao.updateById(questionBank);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
