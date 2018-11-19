package co.bugu.tes.scene.service.impl;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.scene.dao.SceneDao;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.service.ISceneService;
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
 * @create 2018-11-19 17:52
 */
@Service
public class SceneServiceImpl implements ISceneService {
    @Autowired
    SceneDao sceneDao;

    private Logger logger = LoggerFactory.getLogger(SceneServiceImpl.class);

    private static String ORDER_BY = "update_time DESC";

    @Override
    public long add(Scene scene) {
        //todo 校验参数
        scene.setIsDel(DelFlagEnum.NO.getCode());

        Date now = new Date();
        scene.setCreateTime(now);
        scene.setUpdateTime(now);
        sceneDao.insert(scene);
        return scene.getId();
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
        scene.setUpdateSceneId(operatorId);
        int num = sceneDao.updateById(scene);

        logger.debug("将 {} 条 数据删除", num);
        return num;
    }

}
