package co.bugu.tes.scene.agent;

import co.bugu.common.enums.DelFlagEnum;
import co.bugu.tes.paper.agent.PaperAgent;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.enums.PaperStatusEnum;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.enums.SceneStatusEnum;
import co.bugu.tes.scene.service.ISceneService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author daocers
 * @Date 2018/11/28:11:57
 * @Description:
 */
@Service
public class SceneAgent {
    private Logger logger = LoggerFactory.getLogger(SceneAgent.class);


    @Autowired
    ISceneService sceneService;
    @Autowired
    IPaperService paperService;
    @Autowired
    PaperAgent paperAgent;


    /**
     * 封场
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 14:14
     */
    public boolean closeAllScene() throws Exception {
        Scene query = new Scene();
        query.setStatus(SceneStatusEnum.ON.getCode());
        query.setIsDel(DelFlagEnum.NO.getCode());
        List<Scene> scenes = sceneService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(scenes)) {
            Date now = new Date();
            for (Scene scene : scenes) {
//                结束五分钟之后，自动封场
                if(now.getTime() > scene.getCloseTime().getTime() + 300000){
                    closeScene(scene);
                }
            }
        }
        return true;
    }

    /**
     * 封场 记录得分，更新场次状态
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 11:58
     */
    public boolean closeScene(Scene scene) throws Exception {
        Long sceneId = scene.getId();

//        获取已经提交 状态的试卷信息
        Paper query = new Paper();
        query.setSceneId(sceneId);
        query.setIsDel(DelFlagEnum.NO.getCode());
        query.setStatus(PaperStatusEnum.COMMITED.getCode());
        List<Paper> papers = paperService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(papers)) {
            for (Paper paper : papers) {
                Long paperId = paper.getId();
                double score = paperAgent.computeScore(scene, paper);
                logger.info("得分， {}， 试卷id: {}", new Object[]{score, paperId});
            }
        }
//        计算完得分，更新场次信息
        scene.setStatus(SceneStatusEnum.CLOSED.getCode());
        scene.setUpdateTime(new Date());
        sceneService.updateById(scene);
        return true;
    }

    /**
     * 修改场次状态
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 14:28
     */
    public void changeReadyToOn() {
        Scene query = new Scene();
        query.setIsDel(DelFlagEnum.NO.getCode());
        query.setStatus(SceneStatusEnum.READY.getCode());

        List<Scene> scenes = sceneService.findByCondition(query);
        if (CollectionUtils.isNotEmpty(scenes)) {
            Date now = new Date();
            for (Scene scene : scenes) {

//                开场时间早于当前时间 且 当前时间早于结束时间，设置为开场状态
                if(scene.getOpenTime().before(now) && now.before(scene.getCloseTime())){
                    scene.setUpdateTime(now);
                    scene.setStatus(SceneStatusEnum.ON.getCode());
                    sceneService.updateById(scene);
                }
            }
        }
    }
}
