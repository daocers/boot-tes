package co.bugu.tes.joinInfo.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.exception.UserException;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.service.IBranchService;
import co.bugu.tes.department.domain.Department;
import co.bugu.tes.department.service.IDepartmentService;
import co.bugu.tes.joinInfo.dao.JoinInfoDao;
import co.bugu.tes.joinInfo.domain.JoinInfo;
import co.bugu.tes.joinInfo.dto.JoinInfoQueryDto;
import co.bugu.tes.joinInfo.service.IJoinInfoService;
import co.bugu.tes.manager.enums.ManagerTypeEnum;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.station.domain.Station;
import co.bugu.tes.station.service.IStationService;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    IBranchService branchService;
    @Autowired
    IDepartmentService departmentService;
    @Autowired
    IStationService stationService;

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

    @Override
    public List<JoinInfo> batchAdd(List<JoinInfo> list) {
        joinInfoDao.batchAdd(list);
        return list;
    }

    @Override
    public List<JoinInfo> saveJoinInfo(Scene scene, List<Long> branchIds, List<Long> departmentIds, List<Long> stationIds) throws UserException {
        Long sceneId = scene.getId();
//        先删除所有的，然后批量添加
        joinInfoDao.deleteBySceneId(sceneId);

        Long userId = UserUtil.getCurrentUser().getId();
        List<JoinInfo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(branchIds)) {
            for (Long branchId : branchIds) {
                Branch branch = branchService.findById(branchId);
                JoinInfo info = new JoinInfo();
                info.setIsDel(DelFlagEnum.NO.getCode());
                info.setTargetId(branchId);
                info.setTargetCode(branch.getCode());
                info.setTargetName(branch.getName());
                info.setType(ManagerTypeEnum.BRANCH.getCode());
                info.setSceneId(sceneId);
                info.setCreateUserId(userId);
                info.setUpdateUserId(userId);
                info.setOpenTime(scene.getOpenTime());
                list.add(info);

            }
        }

        if (CollectionUtils.isNotEmpty(departmentIds)) {
            for (Long departmentId : departmentIds) {
                Department department = departmentService.findById(departmentId);
                JoinInfo info = new JoinInfo();
                info.setIsDel(DelFlagEnum.NO.getCode());
                info.setTargetId(departmentId);
                info.setType(ManagerTypeEnum.DEPARTMENT.getCode());
                info.setTargetCode(department.getCode());
                info.setTargetName(department.getName());
                info.setSceneId(sceneId);
                info.setCreateUserId(userId);
                info.setUpdateUserId(userId);
                info.setOpenTime(scene.getOpenTime());
                list.add(info);
            }
        }

        if (CollectionUtils.isNotEmpty(stationIds)) {
            for (Long stationId : stationIds) {
                Station station = stationService.findById(stationId);
                JoinInfo info = new JoinInfo();
                info.setIsDel(DelFlagEnum.NO.getCode());
                info.setTargetId(stationId);
                info.setTargetCode(station.getCode());
                info.setTargetName(station.getName());
                info.setType(ManagerTypeEnum.STATION.getCode());
                info.setSceneId(sceneId);
                info.setCreateUserId(userId);
                info.setUpdateUserId(userId);
                info.setOpenTime(scene.getOpenTime());
                list.add(info);
            }
        }

//        如果都为空，直接添加一条数据占位，表示不按照部门，岗位，分行等数据区分
        if(CollectionUtils.isEmpty(branchIds) && CollectionUtils.isEmpty(stationIds) && CollectionUtils.isEmpty(departmentIds)){
            JoinInfo info = new JoinInfo();
            info.setIsDel(DelFlagEnum.NO.getCode());
            info.setSceneId(sceneId);
            info.setCreateUserId(userId);
            info.setUpdateUserId(userId);
            info.setOpenTime(scene.getOpenTime());
            joinInfoDao.insert(info);
            list.add(info);
            return list;
        }
        if (CollectionUtils.isNotEmpty(list)) {
            joinInfoDao.batchAdd(list);
        }
        return list;
    }

    @Override
    public List<JoinInfo> findByUserInfo(JoinInfoQueryDto queryDto) {
        return joinInfoDao.findByUserInfo(queryDto);
    }

}
