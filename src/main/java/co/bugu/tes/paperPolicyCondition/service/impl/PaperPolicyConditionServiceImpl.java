package co.bugu.tes.paperPolicyCondition.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.paperPolicyCondition.dao.PaperPolicyConditionDao;
import co.bugu.tes.paperPolicyCondition.domain.PaperPolicyCondition;
import co.bugu.tes.paperPolicyCondition.service.IPaperPolicyConditionService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import java.util.Date;
import java.util.List;

/**
* @author daocers
* @create 2019-05-07 10:25
*/
@Service
public class PaperPolicyConditionServiceImpl implements IPaperPolicyConditionService {
    @Autowired
    PaperPolicyConditionDao paperPolicyConditionDao;

    private static Logger logger = LoggerFactory.getLogger(PaperPolicyConditionServiceImpl.class);

    private static String ORDER_BY = "paper_policy_id DESC";

    @Override
    public long add(PaperPolicyCondition paperPolicyCondition) {
        //todo 校验参数
        paperPolicyCondition.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        paperPolicyCondition.setCreateTime(now);
        paperPolicyCondition.setUpdateTime(now);
        paperPolicyConditionDao.insert(paperPolicyCondition);
        return paperPolicyCondition.getId();
}

    @Override
    public int updateById(PaperPolicyCondition paperPolicyCondition) {
        logger.debug("paperPolicyCondition updateById, 参数： {}", JSON.toJSONString(paperPolicyCondition, true));
        Preconditions.checkNotNull(paperPolicyCondition.getId(), "id不能为空");
        paperPolicyCondition.setUpdateTime(new Date());
        return paperPolicyConditionDao.updateById(paperPolicyCondition);
    }

    @Override
    public List<PaperPolicyCondition> findByCondition(PaperPolicyCondition paperPolicyCondition) {
        logger.debug("paperPolicyCondition findByCondition, 参数： {}", JSON.toJSONString(paperPolicyCondition, true));
        PageHelper.orderBy(ORDER_BY);
        List<PaperPolicyCondition> paperPolicyConditions = paperPolicyConditionDao.findByObject(paperPolicyCondition);

        logger.debug("查询结果， {}", JSON.toJSONString(paperPolicyConditions, true));
        return paperPolicyConditions;
    }

    @Override
    public List<PaperPolicyCondition> findByCondition(Integer pageNum, Integer pageSize, PaperPolicyCondition paperPolicyCondition) {
        logger.debug("paperPolicyCondition findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(paperPolicyCondition, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<PaperPolicyCondition> paperPolicyConditions = paperPolicyConditionDao.findByObject(paperPolicyCondition);

        logger.debug("查询结果， {}", JSON.toJSONString(paperPolicyConditions, true));
        return paperPolicyConditions;
    }

    @Override
    public PageInfo<PaperPolicyCondition> findByConditionWithPage(Integer pageNum, Integer pageSize, PaperPolicyCondition paperPolicyCondition) {
        logger.debug("paperPolicyCondition findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(paperPolicyCondition, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<PaperPolicyCondition> paperPolicyConditions = paperPolicyConditionDao.findByObject(paperPolicyCondition);

        logger.debug("查询结果， {}", JSON.toJSONString(paperPolicyConditions, true));
        return new PageInfo<>(paperPolicyConditions);
    }

    @Override
    public PaperPolicyCondition findById(Long paperPolicyConditionId) {
        logger.debug("paperPolicyCondition findById, 参数 paperPolicyConditionId: {}", paperPolicyConditionId);
        PaperPolicyCondition paperPolicyCondition = paperPolicyConditionDao.selectById(paperPolicyConditionId);

        logger.debug("查询结果： {}", JSON.toJSONString(paperPolicyCondition, true));
        return paperPolicyCondition;
    }

    @Override
    public int deleteById(Long paperPolicyConditionId, Long operatorId) {
        logger.debug("paperPolicyCondition 删除， 参数 paperPolicyConditionId : {}", paperPolicyConditionId);
        PaperPolicyCondition paperPolicyCondition = new PaperPolicyCondition();
        paperPolicyCondition.setId(paperPolicyConditionId);
        paperPolicyCondition.setIsDel(DelFlagEnum.YES.getCode());
        paperPolicyCondition.setUpdateTime(new Date());
        paperPolicyCondition.setUpdateUserId(operatorId);
        int num = paperPolicyConditionDao.updateById(paperPolicyCondition);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

    @Override
    public List<PaperPolicyCondition> batchAdd(List<PaperPolicyCondition> conditionList) {
        logger.debug("PaperPolicyCondition批量添加， 参数：{}", JSON.toJSONString(conditionList, true));
        paperPolicyConditionDao.batchAdd(conditionList);
        return conditionList;
    }

    @Override
    public int deleteAll() {
        logger.debug("开始删除全部试卷策略校验数据");
        int num = paperPolicyConditionDao.deleteAll();
        return num;

    }

}
