package co.bugu.tes.paperPolicy.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.paperPolicy.dao.PaperPolicyDao;
import co.bugu.tes.paperPolicy.domain.PaperPolicy;
import co.bugu.tes.paperPolicy.service.IPaperPolicyService;
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
* @create 2019-04-28 17:08
*/
@Service
public class PaperPolicyServiceImpl implements IPaperPolicyService {
    @Autowired
    PaperPolicyDao paperPolicyDao;

    private static Logger logger = LoggerFactory.getLogger(PaperPolicyServiceImpl.class);

    private static String ORDER_BY = "id desc";

    @Override
    public long add(PaperPolicy paperPolicy) {
        //todo 校验参数
        paperPolicy.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        paperPolicy.setCreateTime(now);
        paperPolicy.setUpdateTime(now);
        paperPolicyDao.insert(paperPolicy);
        return paperPolicy.getId();
}

    @Override
    public int updateById(PaperPolicy paperPolicy) {
        logger.debug("paperPolicy updateById, 参数： {}", JSON.toJSONString(paperPolicy, true));
        Preconditions.checkNotNull(paperPolicy.getId(), "id不能为空");
        paperPolicy.setUpdateTime(new Date());
        return paperPolicyDao.updateById(paperPolicy);
    }

    @Override
    public List<PaperPolicy> findByCondition(PaperPolicy paperPolicy) {
        logger.debug("paperPolicy findByCondition, 参数： {}", JSON.toJSONString(paperPolicy, true));
        PageHelper.orderBy(ORDER_BY);
        List<PaperPolicy> paperPolicys = paperPolicyDao.findByObject(paperPolicy);

        logger.debug("查询结果， {}", JSON.toJSONString(paperPolicys, true));
        return paperPolicys;
    }

    @Override
    public List<PaperPolicy> findByCondition(Integer pageNum, Integer pageSize, PaperPolicy paperPolicy) {
        logger.debug("paperPolicy findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(paperPolicy, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<PaperPolicy> paperPolicys = paperPolicyDao.findByObject(paperPolicy);

        logger.debug("查询结果， {}", JSON.toJSONString(paperPolicys, true));
        return paperPolicys;
    }

    @Override
    public PageInfo<PaperPolicy> findByConditionWithPage(Integer pageNum, Integer pageSize, PaperPolicy paperPolicy) {
        logger.debug("paperPolicy findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(paperPolicy, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<PaperPolicy> paperPolicys = paperPolicyDao.findByObject(paperPolicy);

        logger.debug("查询结果， {}", JSON.toJSONString(paperPolicys, true));
        return new PageInfo<>(paperPolicys);
    }

    @Override
    public PaperPolicy findById(Long paperPolicyId) {
        logger.debug("paperPolicy findById, 参数 paperPolicyId: {}", paperPolicyId);
        PaperPolicy paperPolicy = paperPolicyDao.selectById(paperPolicyId);

        logger.debug("查询结果： {}", JSON.toJSONString(paperPolicy, true));
        return paperPolicy;
    }

    @Override
    public int deleteById(Long paperPolicyId, Long operatorId) {
        logger.debug("paperPolicy 删除， 参数 paperPolicyId : {}", paperPolicyId);
        PaperPolicy paperPolicy = new PaperPolicy();
        paperPolicy.setId(paperPolicyId);
        paperPolicy.setIsDel(DelFlagEnum.YES.getCode());
        paperPolicy.setUpdateTime(new Date());
        paperPolicy.setUpdateUserId(operatorId);
        int num = paperPolicyDao.updateById(paperPolicy);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
