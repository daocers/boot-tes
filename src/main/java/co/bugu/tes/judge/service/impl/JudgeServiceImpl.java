package co.bugu.tes.judge.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.answer.dto.AnswerDto4GenPaper;
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

import java.util.ArrayList;
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

    @Override
    public List<Judge> batchAdd(List<List<String>> data, long userId, Long bankId, Long stationId, Long branchId, Long departmentId, Integer publicFlag) {

//        todo  校验数据完整性
        Date now = new Date();
        List<Judge> judges = new ArrayList<>(data.size());
        for(List<String> list: data){
            int size = list.size();
            Judge judge = new Judge();
            judge.setIsDel(DelFlagEnum.NO.getCode());
            judge.setUpdateTime(now);
            judge.setCreateTime(now);
            judge.setCreateUserId(userId);
            judge.setUpdateUserId(userId);
            judge.setTitle(list.get(0));
            judge.setBankId(bankId);
            judge.setStationId(stationId);
            judge.setBranchId(branchId);
            judge.setDepartmentId(departmentId);
            judge.setStatus(1);
            judge.setPublicFlag(publicFlag);
            judge.setAnswer(list.get(1));
            judge.setExtraInfo(list.get(2));

            //            根据实际情况添加属性
            if(size > 8){
                judge.setAttr1(Integer.valueOf(list.get(8)));
            }else{
                judge.setAttr1(-1);
            }
            if(size > 9){
                judge.setAttr2(Integer.valueOf(list.get(9)));
            }else{
                judge.setAttr2(-1);
            }
            if(size > 10){
                judge.setAttr3(Integer.valueOf(list.get(10)));
            }else{
                judge.setAttr3(-1);
            }
            if(size > 11){
                judge.setAttr4(Integer.valueOf(list.get(11)));
            }else{
                judge.setAttr4(-1);
            }
            if(size > 12){
                judge.setAttr5(Integer.valueOf(list.get(12)));
            }else{
                judge.setAttr5(-1);
            }
            
            judges.add(judge);

        }

        judgeDao.batchAdd(judges);
        return judges;
    }

    @Override
    public List<AnswerDto4GenPaper> getAllIds(Judge judge) {
        return judgeDao.getAllIds(judge);
    }

}
