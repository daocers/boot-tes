package co.bugu.tes.judge.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.judge.dao.JudgeDao;
import co.bugu.tes.judge.domain.Judge;
import co.bugu.tes.judge.service.IJudgeService;
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
 * @create 2018-11-20 17:15
 */
@Service
public class JudgeServiceImpl implements IJudgeService {
    @Autowired
    JudgeDao judgeDao;

    private Logger logger = LoggerFactory.getLogger(JudgeServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(Judge judge) {
        //todo 校验参数
        judge.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        judge.setCreateTime(now);
        judge.setUpdateTime(now);
        judgeDao.insert(judge);
        return judge.getId();
    }

    @Override
    public int updateById(Judge judge) {
        logger.debug("judge updateById, 参数： {}", JSON.toJSONString(judge, true));
        Preconditions.checkNotNull(judge.getId(), "id不能为空");
        judge.setUpdateTime(new Date());
        return judgeDao.updateById(judge);
    }

    @Override
    public List<Judge> findByCondition(Judge judge) {
        logger.debug("judge findByCondition, 参数： {}", JSON.toJSONString(judge, true));
        PageHelper.orderBy(ORDER_BY);
        List<Judge> judges = judgeDao.findByObject(judge);

        logger.debug("查询结果， {}", JSON.toJSONString(judges, true));
        return judges;
    }

    @Override
    public List<Judge> findByCondition(Integer pageNum, Integer pageSize, Judge judge) {
        logger.debug("judge findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(judge, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Judge> judges = judgeDao.findByObject(judge);

        logger.debug("查询结果， {}", JSON.toJSONString(judges, true));
        return judges;
    }

    @Override
    public PageInfo<Judge> findByConditionWithPage(Integer pageNum, Integer pageSize, Judge judge) {
        logger.debug("judge findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(judge, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Judge> judges = judgeDao.findByObject(judge);

        logger.debug("查询结果， {}", JSON.toJSONString(judges, true));
        return new PageInfo<>(judges);
    }

    @Override
    public Judge findById(Long judgeId) {
        logger.debug("judge findById, 参数 judgeId: {}", judgeId);
        Judge judge = judgeDao.selectById(judgeId);

        logger.debug("查询结果： {}", JSON.toJSONString(judge, true));
        return judge;
    }

    @Override
    public int deleteById(Long judgeId, Long operatorId) {
        logger.debug("judge 删除， 参数 judgeId : {}", judgeId);
        Judge judge = new Judge();
        judge.setId(judgeId);
        judge.setIsDel(DelFlagEnum.YES.getCode());
        judge.setUpdateTime(new Date());
        judge.setUpdateUserId(operatorId);
        int num = judgeDao.updateById(judge);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
