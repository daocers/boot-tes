package co.bugu;

import co.bugu.config.filter.TokenFilter;
import com.thetransactioncompany.cors.CORSFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 项目启动类
 */
@SpringBootApplication
@EnableTransactionManagement

@Configuration
@MapperScan("co.bugu.*.*.dao")
@EnableSwagger2
//public class Application {
//    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }
//
//    /**
//     * 过滤器配置
//     *
//     * @param
//     * @return
//     * @auther daocers
//     * @date 2018/12/3 10:31
//     */
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(new CORSFilter());
//        bean.setName("cors");
//        bean.addUrlPatterns("/*");
//        return bean;
//    }
//}


public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
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


    @Bean
    public FilterRegistrationBean tokenFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new TokenFilter());
        bean.setName("tokenFilter");
        bean.addUrlPatterns("/*");
        return bean;
    }


}
