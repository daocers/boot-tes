package co.bugu.tes.task;

import co.bugu.tes.questionStat.service.IQuestionStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author daocers
 * @Date 2019/5/9:12:16
 * @Description:
 */
@Component
public class StatTask {
    private Logger logger = LoggerFactory.getLogger(StatTask.class);
    @Autowired
    IQuestionStatService questionStatService;


    @Scheduled(fixedRate = 60000)
    public void processQuestionStat() {
        logger.info("开始统计试题信息");
        try {
            questionStatService.getAnswerAndProcessStat(100);
        } catch (Exception e) {
            logger.error("统计试题错误信息失败", e);
        }
    }
}
