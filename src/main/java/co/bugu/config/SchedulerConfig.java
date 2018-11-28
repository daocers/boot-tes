package co.bugu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @Author daocers
 * @Date 2018/11/28:13:59
 * @Description:
 */
// 开启定时任务支持
@EnableScheduling
@Configuration
public class SchedulerConfig {

    //    设置spring task使用的线程池，避免默认的单线程处理
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("tes-hn-");
        return scheduler;
    }
}
