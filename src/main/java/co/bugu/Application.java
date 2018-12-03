package co.bugu;

import com.thetransactioncompany.cors.CORSFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 项目启动类
 */
@SpringBootApplication
@EnableTransactionManagement

@Configuration
@MapperScan("co.bugu.*.*.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }



    /**
     * 过滤器配置
     *
     * @param
     * @return
     * @auther daocers
     * @date 2018/12/3 10:31
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new CORSFilter());
        bean.setName("cors");
        bean.addUrlPatterns("/*");
        return bean;
    }


//    @Bean
//    public ApplicationContextUtil applicationContextUtil() {
//        return new ApplicationContextUtil();
//    }

}
