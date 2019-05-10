package co.bugu.tes.single.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.prop.TesConfig;
import co.bugu.tes.answer.dto.AnswerDto4GenPaper;
import co.bugu.tes.single.dao.SingleDao;
import co.bugu.tes.single.domain.Single;
import co.bugu.tes.single.service.ISingleService;
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
import java.util.Map;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class SingleServiceImpl implements ISingleService {
    @Autowired
    SingleDao singleDao;

    @Autowired
    TesConfig config;

    private Logger logger = LoggerFactory.getLogger(SingleServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(Single single) {
        //todo 校验参数
        single.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        single.setCreateTime(now);
        single.setUpdateTime(now);
        singleDao.insert(single);
        return single.getId();
    }

    @Override
    public int updateById(Single single) {
        logger.debug("single updateById, 参数： {}", JSON.toJSONString(single, true));
        Preconditions.checkNotNull(single.getId(), "id不能为空");
        single.setUpdateTime(new Date());
        return singleDao.updateById(single);
    }

    @Override
    public List<Single> findByCondition(Single single) {
        logger.debug("single findByCondition, 参数： {}", JSON.toJSONString(single, true));
        PageHelper.orderBy(ORDER_BY);
        List<Single> singles = singleDao.findByObject(single);

        logger.debug("查询结果， {}", JSON.toJSONString(singles, true));
        return singles;
    }

    @Override
    public List<Single> findByCondition(Integer pageNum, Integer pageSize, Single single) {
        logger.debug("single findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(single, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Single> singles = singleDao.findByObject(single);

        logger.debug("查询结果， {}", JSON.toJSONString(singles, true));
        return singles;
    }

    @Override
    public PageInfo<Single> findByConditionWithPage(Integer pageNum, Integer pageSize, Single single) {
        logger.debug("single findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(single, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Single> singles = singleDao.findByObject(single);

        logger.debug("查询结果， {}", JSON.toJSONString(singles, true));
        return new PageInfo<>(singles);
    }

    @Override
    public Single findById(Long singleId) {
        logger.debug("single findById, 参数 singleId: {}", singleId);
        Single single = singleDao.selectById(singleId);

        logger.debug("查询结果： {}", JSON.toJSONString(single, true));
        return single;
    }

    @Override
    public int deleteById(Long singleId, Long operatorId) {
        logger.debug("single 删除， 参数 singleId : {}", singleId);
        Single single = new Single();
        single.setId(singleId);
        single.setIsDel(DelFlagEnum.YES.getCode());
        single.setUpdateTime(new Date());
        single.setUpdateUserId(operatorId);
        int num = singleDao.updateById(single);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

    @Override
    public List<Single> batchAdd(List<List<String>> data, long userId, Long bankId, Long stationId, Long branchId, Long departmentId, Integer publicFlag) throws Exception {
//        todo  校验数据完整性
        Date now = new Date();
        List<Single> singles = new ArrayList<>(data.size());
        Map<String, Integer> diffMap = config.getDifficultyInfo();
        Map<String, Integer> busiMap = config.getBusiTypeInfo();

        int line = 2;
        for(List<String> list: data){
            int size = list.size();
            Single single = new Single();
            single.setIsDel(DelFlagEnum.NO.getCode());
            single.setUpdateTime(now);
            single.setCreateTime(now);
            single.setCreateUserId(userId);
            single.setUpdateUserId(userId);
            single.setTitle(list.get(0));
            single.setBankId(bankId);
            single.setStationId(stationId);
            single.setBranchId(branchId);
            single.setDepartmentId(departmentId);
            single.setStatus(1);
            single.setPublicFlag(publicFlag);
            List<String> content = new ArrayList<>();
            content.add(list.get(1));
            content.add(list.get(2));
            content.add(list.get(3));
            content.add(list.get(4));
            content.add(list.get(5));
            single.setContent(JSON.toJSONString(content, true));
            single.setAnswer(list.get(6));
            single.setExtraInfo(list.get(7));

//            根据实际情况添加属性
            if(size > 8){
                if(!busiMap.containsKey(list.get(8))){
                    logger.warn("第八列属性不合法，请检查");
                    throw new Exception("第" + line + "行第八列属性不合法");
                }
                single.setAttr1(busiMap.get(list.get(8)));
            }else{

                single.setAttr1(-1);
            }
            if(size > 9){
                if(!diffMap.containsKey(list.get(9))){
                    logger.warn("第九列属性不合法，请检查");
                    throw new Exception("第" + line + "行第九列属性不合法");
                }
                single.setAttr2(diffMap.get(list.get(9)));
            }else{
                single.setAttr2(-1);
            }
            if(size > 10){
                single.setAttr3(Integer.valueOf(list.get(10)));
            }else{
                single.setAttr3(-1);
            }
            if(size > 11){
                single.setAttr4(Integer.valueOf(list.get(11)));
            }else{
                single.setAttr4(-1);
            }
            if(size > 12){
                single.setAttr5(Integer.valueOf(list.get(12)));
            }else{
                single.setAttr5(-1);
            }
            singles.add(single);
            line++;

        }

        singleDao.batchAdd(singles);
        return singles;
    }

    @Override
    public List<AnswerDto4GenPaper> getAllIds(Single single) {
        return singleDao.getAllIds(single);
    }

}
