package com.optimagrowth.organization.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        UserContext context = UserContextHolder.getContext();
        context.setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID) );
        context.setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
        context.setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        context.setOrgId(httpServletRequest.getHeader(UserContext.ORGANIZATION_ID));

        log.info("UserContextFilter Correlation id: {}", context.getCorrelationId());

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
