package com.yao.base.framework.filter;

import com.yao.base.framework.constant.CommonConstants;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


@Order(Ordered.HIGHEST_PRECEDENCE) // 最高优先级，最先执行
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            String traceId = httpRequest.getHeader(CommonConstants.HEADER_TRACE_ID);
            if (traceId == null || traceId.isEmpty()) {
                traceId = UUID.randomUUID().toString().replace("-", "");
            }
            MDC.put(CommonConstants.TRACE_ID_KEY, traceId);
            httpResponse.setHeader(CommonConstants.HEADER_TRACE_ID, traceId);
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}