package co.bugu.tes.joinInfo.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.joinInfo.dao.JoinInfoDao;
import co.bugu.tes.joinInfo.domain.JoinInfo;
import co.bugu.tes.joinInfo.service.IJoinInfoService;
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
 * @create 2018-12-16 23:21
 */
@Service
public class JoinInfoServiceImpl implements IJoinInfoService {
    @Autowired
    JoinInfoDao joinInfoDao;

    private Logger logger = LoggerFactory.getLogger(JoinInfoServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(JoinInfo joinInfo) {
        //todo 校验参数
        joinInfo.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        joinInfo.setCreateTime(now);
        joinInfo.setUpdateTime(now);
        joinInfoDao.insert(joinInfo);
        return joinInfo.getId();
    }

    @Override
    public int updateById(JoinInfo joinInfo) {
        logger.debug("joinInfo updateById, 参数： {}", JSON.toJSONString(joinInfo, true));
        Preconditions.checkNotNull(joinInfo.getId(), "id不能为空");
        joinInfo.setUpdateTime(new Date());
        return joinInfoDao.updateById(joinInfo);
    }

    @Override
    public List<JoinInfo> findByCondition(JoinInfo joinInfo) {
        logger.debug("joinInfo findByCondition, 参数： {}", JSON.toJSONString(joinInfo, true));
        PageHelper.orderBy(ORDER_BY);
        List<JoinInfo> joinInfos = joinInfoDao.findByObject(joinInfo);

        logger.debug("查询结果， {}", JSON.toJSONString(joinInfos, true));
        return joinInfos;
    }

    @Override
    public List<JoinInfo> findByCondition(Integer pageNum, Integer pageSize, JoinInfo joinInfo) {
        logger.debug("joinInfo findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(joinInfo, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<JoinInfo> joinInfos = joinInfoDao.findByObject(joinInfo);

        logger.debug("查询结果， {}", JSON.toJSONString(joinInfos, true));
        return joinInfos;
    }

    @Override
    public PageInfo<JoinInfo> findByConditionWithPage(Integer pageNum, Integer pageSize, JoinInfo joinInfo) {
        logger.debug("joinInfo findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(joinInfo, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<JoinInfo> joinInfos = joinInfoDao.findByObject(joinInfo);

        logger.debug("查询结果， {}", JSON.toJSONString(joinInfos, true));
        return new PageInfo<>(joinInfos);
    }

    @Override
    public JoinInfo findById(Long joinInfoId) {
        logger.debug("joinInfo findById, 参数 joinInfoId: {}", joinInfoId);
        JoinInfo joinInfo = joinInfoDao.selectById(joinInfoId);

        logger.debug("查询结果： {}", JSON.toJSONString(joinInfo, true));
        return joinInfo;
    }

    @Override
    public int deleteById(Long joinInfoId, Long operatorId) {
        logger.debug("joinInfo 删除， 参数 joinInfoId : {}", joinInfoId);
        JoinInfo joinInfo = new JoinInfo();
        joinInfo.setId(joinInfoId);
        joinInfo.setIsDel(DelFlagEnum.YES.getCode());
        joinInfo.setUpdateTime(new Date());
        joinInfo.setUpdateUserId(operatorId);
        int num = joinInfoDao.updateById(joinInfo);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
