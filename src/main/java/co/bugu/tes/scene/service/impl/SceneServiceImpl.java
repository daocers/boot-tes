package co.bugu.tes.scene.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.branch.domain.Branch;
import co.bugu.tes.branch.service.IBranchService;
import co.bugu.tes.joinInfo.domain.JoinInfo;
import co.bugu.tes.manager.enums.ManagerTypeEnum;
import co.bugu.tes.scene.dao.SceneDao;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.enums.SceneStatusEnum;
import co.bugu.tes.scene.service.ISceneService;
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
 * @create 2018-11-20 17:15
 */
@Service
public class SceneServiceImpl implements ISceneService {
    @Autowired
    SceneDao sceneDao;
    @Autowired
    IBranchService branchService;

    private Logger logger = LoggerFactory.getLogger(SceneServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(Scene scene) {
        //todo 校验参数
        scene.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        scene.setCreateTime(now);
        scene.setUpdateTime(now);
        scene.setStatus(SceneStatusEnum.READY.getCode());
        sceneDao.insert(scene);
        return scene.getId();
    }

    @Override
    public long add(Scene scene, List<Long> branchIds, List<Long> departmentIds, List<Long> stationIds) {
        scene.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        scene.setCreateTime(now);
        scene.setUpdateTime(now);
        scene.setStatus(SceneStatusEnum.READY.getCode());
        sceneDao.insert(scene);
        Long sceneId = scene.getId();
        List<JoinInfo> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(branchIds)){
            for(Long branchId: branchIds){
                Branch branch = branchService.findById(branchId);
                JoinInfo info = new JoinInfo();
                info.setIsDel(DelFlagEnum.NO.getCode());
                info.setTargetId(branchId);
                info.setTargetCode(branch.getCode());
                info.setType(ManagerTypeEnum.BRANCH.getCode());
                info.setSceneId(sceneId);
                list.add(info);
            }
        }

        if(CollectionUtils.isNotEmpty(departmentIds)){
            for(Long departmentId: departmentIds){
                JoinInfo info = new JoinInfo();
                info.setIsDel(DelFlagEnum.NO.getCode());
                info.setTargetId(departmentId);
                info.setType(ManagerTypeEnum.DEPARTMENT.getCode());
                info.setSceneId(sceneId);
                list.add(info);
            }
        }

        return 0;
    }

    @Override
    public long updateById(Scene scene, List<Long> branchIds, List<Long> departmentIds, List<Long> stationIds) {
        return 0;
    }

    @Override
    public int updateById(Scene scene) {
        logger.debug("scene updateById, 参数： {}", JSON.toJSONString(scene, true));
        Preconditions.checkNotNull(scene.getId(), "id不能为空");
        scene.setUpdateTime(new Date());
        return sceneDao.updateById(scene);
    }

    @Override
    public List<Scene> findByCondition(Scene scene) {
        logger.debug("scene findByCondition, 参数： {}", JSON.toJSONString(scene, true));
        PageHelper.orderBy(ORDER_BY);
        List<Scene> scenes = sceneDao.findByObject(scene);

        logger.debug("查询结果， {}", JSON.toJSONString(scenes, true));
        return scenes;
    }

    @Override
    public List<Scene> findByCondition(Integer pageNum, Integer pageSize, Scene scene) {
        logger.debug("scene findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(scene, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Scene> scenes = sceneDao.findByObject(scene);

        logger.debug("查询结果， {}", JSON.toJSONString(scenes, true));
        return scenes;
    }

    @Override
    public PageInfo<Scene> findByConditionWithPage(Integer pageNum, Integer pageSize, Scene scene) {
        logger.debug("scene findByCondition, 参数 pageNum: {}, pageSize: {}, condition: {}", new Object[]{pageNum, pageSize, JSON.toJSONString(scene, true)});
        PageHelper.startPage(pageNum, pageSize, ORDER_BY);
        List<Scene> scenes = sceneDao.findByObject(scene);

        logger.debug("查询结果， {}", JSON.toJSONString(scenes, true));
        return new PageInfo<>(scenes);
    }

    @Override
    public Scene findById(Long sceneId) {
        logger.debug("scene findById, 参数 sceneId: {}", sceneId);
        Scene scene = sceneDao.selectById(sceneId);

        logger.debug("查询结果： {}", JSON.toJSONString(scene, true));
        return scene;
    }

    @Override
    public int deleteById(Long sceneId, Long operatorId) {
        logger.debug("scene 删除， 参数 sceneId : {}", sceneId);
        Scene scene = new Scene();
        scene.setId(sceneId);
        scene.setIsDel(DelFlagEnum.YES.getCode());
        scene.setUpdateTime(new Date());
        scene.setUpdateUserId(operatorId);
        int num = sceneDao.updateById(scene);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
