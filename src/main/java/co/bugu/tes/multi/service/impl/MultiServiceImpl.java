package co.bugu.tes.multi.service.impl;

import co.bugu.common.enums.DelFlagEnum;
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

import java.util.Date;
import java.util.List;

/**
 * @author daocers
 * @create 2018-11-20 17:15
 */
@Service
public class MultiServiceImpl implements IMultiService {
    @Autowired
    MultiDao multiDao;

    private Logger logger = LoggerFactory.getLogger(MultiServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

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

}
