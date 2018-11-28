package co.bugu.tes.tesk;

import co.bugu.tes.paper.agent.PaperAgent;
import co.bugu.tes.scene.agent.SceneAgent;
import co.bugu.tes.scene.service.ISceneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author daocers
 * @Date 2018/11/28:11:54
 * @Description:
 */
@Component
public class CloseSceneTask {
    private Logger logger = LoggerFactory.getLogger(CloseSceneTask.class);
    @Autowired
    ISceneService sceneService;
    @Autowired
    PaperAgent paperAgent;
    @Autowired
    SceneAgent sceneAgent;

    /**
     * 封场，每分钟执行一次
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 11:56
     */
    @Scheduled(fixedRate = 60000)
    public void closeScene() {
        try {
            logger.info("开始执行封场");
            sceneAgent.closeAllScene();
        } catch (Exception e) {
            logger.error("封场失败", e);
        }
    }

    /**
     * 更新场次状态，从就绪到开场
     * 5s一次
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/11/28 15:05
     */
    @Scheduled(fixedRate = 5000)
    public void changeSceneReadyToOn(){
        try{
            logger.info("开始修改场次状态");
            sceneAgent.changeReadyToOn();
        }catch (Exception e){
            logger.error("修改场次状态失败", e);
        }
    }
}
