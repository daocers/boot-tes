package co.bugu.config.interceptor;

import co.bugu.common.RespDto;
import co.bugu.tes.permission.agent.PermissionAgent;
import co.bugu.tes.user.domain.User;
import co.bugu.util.ApplicationContextUtil;
import co.bugu.util.ThreadLocalUtil;
import co.bugu.util.UserUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

public class PermissionInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = ThreadLocalUtil.getUserToken();
        User user = UserUtil.getCurrentUser();

        String requestUrl = request.getRequestURL().toString();
        PermissionAgent permissionAgent = ApplicationContextUtil.getClass(PermissionAgent.class);
        boolean hasAuth = permissionAgent.checkAuthForUser(user.getId(), requestUrl);
        if (hasAuth) {
            return true;
        } else {
            response.setCharacterEncoding("utf-8");
            Writer writer = response.getWriter();
            writer.write(JSON.toJSONString(RespDto.fail(-1, "您没有相对应的权限")));
            writer.close();
            logger.error("没有访问该url对应的权限， requestUrl: {}", token);
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
