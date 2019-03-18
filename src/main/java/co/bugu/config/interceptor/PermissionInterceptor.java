package co.bugu.config.interceptor;

import co.bugu.tes.role.dto.RoleDto;
import co.bugu.tes.user.domain.User;
import co.bugu.util.ThreadLocalUtil;
import co.bugu.util.UserUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = ThreadLocalUtil.getUserToken();
        User user = UserUtil.getCurrentUser();

        String requestUrl = request.getRequestURL().toString();


        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
