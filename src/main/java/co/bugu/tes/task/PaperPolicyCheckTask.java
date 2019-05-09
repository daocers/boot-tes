package co.bugu.tes.task;

import co.bugu.tes.paperPolicy.agent.PaperPolicyAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author daocers
 * @Date 2019/5/7:00:24
 * @Description:
 */
//@Component
public class PaperPolicyCheckTask {
    private Logger logger = LoggerFactory.getLogger(PaperPolicyCheckTask.class);

    @Autowired
    PaperPolicyAgent paperPolicyAgent;

    @Scheduled(fixedRate = 60000)
    public void checkPaperPolicy() {
        logger.info("开始校验试卷策略");
        try {
            paperPolicyAgent.checkAllPolicyAndRefreshRecord();
        } catch (Exception e) {
            logger.error("校验试卷策略失败", e);
        }
    }
}
