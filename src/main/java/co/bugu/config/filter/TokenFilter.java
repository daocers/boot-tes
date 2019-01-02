package co.bugu.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * filter doFilter不执行filterChain.doFilter()时候，会
 *
 * @Author daocers
 * @Date 2019/1/2:17:36
 * @Description:
 */
public class TokenFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logger.info("什么也不做，什么也不管");
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
