package co.bugu.tes.task;

import co.bugu.util.CodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author daocers
 * @Date 2018/11/29:10:08
 * @Description:
 */
//@Component
public class CodeTask {
    private Logger logger = LoggerFactory.getLogger(CodeTask.class);

    @Scheduled(cron = "0 0 0 * * *")
    public void chanCodeInfo() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String timeInfo = now.format(dateTimeFormatter);
        logger.info("timeInfo:", timeInfo);
        CodeUtil.setDateInfo(timeInfo);

        CodeUtil.setPaperIndex(new AtomicLong(1L));
        CodeUtil.setQuestionIndex(new AtomicLong(1L));
        CodeUtil.setSceneIndex(new AtomicLong(1L));
    }
}
