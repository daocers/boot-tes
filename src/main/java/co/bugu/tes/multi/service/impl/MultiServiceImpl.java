package co.bugu.tes.multi.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.prop.TesConfig;
import co.bugu.tes.answer.dto.AnswerDto4GenPaper;
import co.bugu.tes.multi.dao.MultiDao;
import co.bugu.tes.multi.domain.Multi;
import co.bugu.tes.multi.service.IMultiService;
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
public class MultiServiceImpl implements IMultiService {
    @Autowired
    MultiDao multiDao;

    @Autowired
    TesConfig config;
    private Logger logger = LoggerFactory.getLogger(MultiServiceImpl.class);

    private static String ORDER_BY = "id desc";

    @Override
    public long add(Multi multi) {
        //todo 校验参数
        multi.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        multi.setCreateTime(now);
        multi.setUpdateTime(now);
        multiDao.insert(multi);
        return multi.getId();
    }

    @Override
    public int updateById(Multi multi) {
        logger.debug("multi updateById, 参数： {}", JSON.toJSONString(multi, true));
        Preconditions.checkNotNull(multi.getId(), "id不能为空");
        multi.setUpdateTime(new Date());
        return multiDao.updateById(multi);
    }

    @Override
    public List<Multi> findByCondition(Multi multi) {
        logger.debug("multi findByCondition, 参数： {}", JSON.toJSONString(multi, true));
        PageHelper.orderBy(ORDER_BY);
        List<Multi> multis = multiDao.findByObject(multi);

        logger.debug("查询结果， {}", JSON.toJSONString(multis, true));
        return multis;
    }

    @Override
    public List<Multi> findByCondition(Integer pageNum, Integer pageSize, Multi multi) {
        logger.debug("multi findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(multi, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Multi> multis = multiDao.findByObject(multi);

        logger.debug("查询结果， {}", JSON.toJSONString(multis, true));
        return multis;
    }

    @Override
    public PageInfo<Multi> findByConditionWithPage(Integer pageNum, Integer pageSize, Multi multi) {
        logger.debug("multi findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(multi, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Multi> multis = multiDao.findByObject(multi);

        logger.debug("查询结果， {}", JSON.toJSONString(multis, true));
        return new PageInfo<>(multis);
    }

    @Override
    public Multi findById(Long multiId) {
        logger.debug("multi findById, 参数 multiId: {}", multiId);
        Multi multi = multiDao.selectById(multiId);

        logger.debug("查询结果： {}", JSON.toJSONString(multi, true));
        return multi;
    }

    @Override
    public int deleteById(Long multiId, Long operatorId) {
        logger.debug("multi 删除， 参数 multiId : {}", multiId);
        Multi multi = new Multi();
        multi.setId(multiId);
        multi.setIsDel(DelFlagEnum.YES.getCode());
        multi.setUpdateTime(new Date());
        multi.setUpdateUserId(operatorId);
        int num = multiDao.updateById(multi);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

    @Override
    public List<Multi> batchAdd(List<List<String>> data, long userId, Long bankId, Long stationId, Long branchId, Long departmentId, Integer publicFlag) throws Exception {

//        todo  校验数据完整性
        Date now = new Date();
        List<Multi> multis = new ArrayList<>(data.size());
        Map<String, Integer> busiMap = config.getBusiTypeInfo();
        Map<String, Integer> diffMap = config.getDifficultyInfo();
        int line = 2;
        for (List<String> list : data) {
            int size = list.size();
            Multi multi = new Multi();
            multi.setIsDel(DelFlagEnum.NO.getCode());
            multi.setUpdateTime(now);
            multi.setCreateTime(now);
            multi.setCreateUserId(userId);
            multi.setUpdateUserId(userId);
            multi.setTitle(list.get(0));
            multi.setBankId(bankId);
            multi.setStationId(stationId);
            multi.setBranchId(branchId);
            multi.setDepartmentId(departmentId);
            multi.setStatus(1);
            multi.setPublicFlag(publicFlag);
            List<String> content = new ArrayList<>();
            content.add(list.get(1));
            content.add(list.get(2));
            content.add(list.get(3));
            content.add(list.get(4));
            content.add(list.get(5));
            multi.setContent(JSON.toJSONString(content, true));
            multi.setAnswer(list.get(6));
            multi.setExtraInfo(list.get(7));


            //            根据实际情况添加属性
            if (size > 8) {
                if (!busiMap.containsKey(list.get(8))) {
                    logger.warn("第八列属性不合法，请检查");
                    throw new Exception("第" + line + "行第八列属性不合法");
                }
                multi.setAttr1(busiMap.get(list.get(8)));
            } else {

                multi.setAttr1(-1);
            }
            if (size > 9) {
                if (!diffMap.containsKey(list.get(9))) {
                    logger.warn("第九列属性不合法，请检查");
                    throw new Exception("第" + line + "行第九列属性不合法");
                }
                multi.setAttr2(diffMap.get(list.get(9)));
            } else {
                multi.setAttr2(-1);
            }

            if (size > 10) {
                multi.setAttr3(Integer.valueOf(list.get(10)));
            } else {
                multi.setAttr3(-1);
            }
            if (size > 11) {
                multi.setAttr4(Integer.valueOf(list.get(11)));
            } else {
                multi.setAttr4(-1);
            }
            if (size > 12) {
                multi.setAttr5(Integer.valueOf(list.get(12)));
            } else {
                multi.setAttr5(-1);
            }
            multis.add(multi);
            line++;

        }

        multiDao.batchAdd(multis);
        return multis;
    }

    @Override
    public List<AnswerDto4GenPaper> getAllIds(Multi multi) {
        return multiDao.getAllIds(multi);
    }

}
