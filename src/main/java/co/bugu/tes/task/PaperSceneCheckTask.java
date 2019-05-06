package co.bugu.tes.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author daocers
 * @Date 2019/5/7:00:24
 * @Description:
 */
@Component
public class PaperSceneCheckTask {
    private Logger logger = LoggerFactory.getLogger(PaperSceneCheckTask.class);

    @Scheduled(fixedRate = 30000)
    public void checkPaperPolicy() {
//        todo 校验每个试卷策略对每个题库的可用性，然后保存信息到数据库
        logger.info("开始校验试卷策略");
    }
}
