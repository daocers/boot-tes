package co.bugu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author daocers
 * @Date 2018/11/20:10:15
 * @Description:
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
//   添加拦截器
    public void addInterceptors(InterceptorRegistry registry){

    }

//    跨域访问配置
    public void addCorsMappings(CorsRegistry registry){

    }

//    格式化
    public void addFormatters(FormatterRegistry registry){

    }


//    uri到视图的映射
    public void addViewControllers(ViewControllerRegistry registry){

    }
}
