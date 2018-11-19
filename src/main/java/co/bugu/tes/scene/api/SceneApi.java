package co.bugu.tes.scene.api;

import co.bugu.common.RespDto;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.service.ISceneService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据api
 *
 * @author daocers
 * @create 2018-11-19 17:52
 */
@RestController
@RequestMapping("/scene/api")
public class SceneApi {
    private Logger logger = LoggerFactory.getLogger(SceneApi.class);

    @Autowired
    ISceneService sceneService;

    /**
     * 条件查询
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 17:52
     */
    @RequestMapping(value = "/findByCondition")
    public RespDto<PageInfo<Scene>> findByCondition(Integer pageNum, Integer pageSize, @RequestBody Scene scene) {
        try {
            logger.debug("条件查询， 参数: {}", JSON.toJSONString(scene, true));
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            List<Scene> list = sceneService.findByCondition(pageNum, pageSize, scene);
            PageInfo<Scene> pageInfo = new PageInfo<>(list);
            logger.info("查询到数据： {}", JSON.toJSONString(pageInfo, true));
            return RespDto.success(pageInfo);
        } catch (Exception e) {
            logger.error("findByCondition  失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 保存
     *
     * @param scene
     * @return co.bugu.common.RespDto<java.lang.Boolean>
     * @author daocers
     * @date 2018-11-19 17:52
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespDto<Boolean> saveScene(@RequestBody Scene scene) {
        try {
            Long sceneId = scene.getId();
            if(null == sceneId){
                logger.debug("保存， saveScene, 参数： {}", JSON.toJSONString(scene, true));
                sceneId = sceneService.add(scene);
                logger.info("新增 成功， id: {}", sceneId);
            }else{
                sceneService.updateById(scene);
                logger.debug("更新成功", JSON.toJSONString(scene, true));
            }
            return RespDto.success(sceneId != null);
        } catch (Exception e) {
            logger.error("保存 scene 失败", e);
            return RespDto.fail();
        }
    }


    /**
     * 获取详情
     *
     * @param id
     * @return co.bugu.common.RespDto<co.bugu.tes.scene.domain.Scene>
     * @author daocers
     * @date 2018-11-19 17:52
     */
    @RequestMapping(value = "/findById")
    public RespDto<Scene> findById(Long id) {
        try {
            logger.info("findById, id： {}", id);
            Scene scene = sceneService.findById(id);
            return RespDto.success(scene);
        } catch (Exception e) {
            logger.error("获取详情失败", e);
            return RespDto.fail();
        }
    }

    /**
     * 删除，软删除，更新数据库删除标志
     *
     * @param
     * @return
     * @author daocers
     * @date 2018-11-19 17:52
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespDto<Boolean> delete(Long id, Long operatorId) {
        try {
            logger.debug("准备删除， 参数: {}", id);
            Preconditions.checkArgument(id != null, "id不能为空");
            int count = sceneService.deleteById(id, operatorId);

            return RespDto.success(count == 1);
        } catch (Exception e) {
            logger.error("删除 失败", e);
            return RespDto.fail();
        }
    }
}

