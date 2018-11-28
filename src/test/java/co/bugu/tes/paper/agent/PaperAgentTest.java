package co.bugu.tes.paper.agent;

import co.bugu.Application;
import co.bugu.tes.paper.domain.Paper;
import co.bugu.tes.paper.service.IPaperService;
import co.bugu.tes.scene.domain.Scene;
import co.bugu.tes.scene.service.ISceneService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author daocers
 * @Date 2018/11/28:15:53
 * @Description:
 */

//, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)  加上这段是为了保证websocket环境下测试不报错
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PaperAgentTest {

    @Autowired
    PaperAgent paperAgent;
    @Autowired
    ISceneService sceneService;
    @Autowired
    IPaperService paperService;
    @Test
    public void computeScore() throws Exception {
        Scene scene = sceneService.findById(1L);
        Paper paper = paperService.findById(5L);
        paperAgent.computeScore(scene, paper);
    }
}