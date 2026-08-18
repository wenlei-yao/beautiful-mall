package com.yao.gateway.filter;

import com.yao.base.framework.constant.CommonConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class TraceIdGatewayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = request.getHeaders().getFirst(CommonConstants.HEADER_TRACE_ID);
        if (traceId == null) {
            traceId = generateTraceId();
        }

        ServerHttpRequest mutateRequest = request.mutate()
                .header(CommonConstants.HEADER_TRACE_ID,traceId)
                .build();

        exchange.getResponse().getHeaders().add(CommonConstants.HEADER_TRACE_ID,traceId);

        return chain.filter(exchange.mutate().request(mutateRequest).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
