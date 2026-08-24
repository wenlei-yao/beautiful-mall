package com.yao.gateway.filter;

import com.yao.base.framework.common.Result;
import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.constant.CommonConstants;
import com.yao.base.framework.redis.RedisService;
import com.yao.base.framework.util.json.JSONUtils;
import com.yao.base.framework.util.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AuthGatewayFilter implements GlobalFilter, Ordered {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final String TOKEN_BLACK_LIST = "token:black:";

    private static final List<String> WHITE_LIST = Arrays.asList(
            "/api/seckill/activity/**",
            "/api/seckill/product/**"
    );

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public AuthGatewayFilter(JwtUtil jwtUtil, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (isWhiteListed(path)){
            return chain.filter(exchange);
        }

        String authorization = request.getHeaders().getFirst(CommonConstants.HEADER_AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(CommonConstants.TOKEN_PREFIX)){
            return unauthorized(exchange);
        }
        String token = authorization.substring(CommonConstants.TOKEN_PREFIX.length());
        //退出登录场景，未到期 token 直接失效
        if (redisService.hasKey(TOKEN_BLACK_LIST + token)){
            return unauthorized(exchange);
        }
        Long userId;
        try {
            userId = jwtUtil.getUserId(token);
        }catch (Exception e){
            log.error("token 解析失败");
            return unauthorized(exchange);
        }
        ServerHttpRequest mutateRequest = request.mutate()
                .header(CommonConstants.HEADER_USER_ID, String.valueOf(userId))
                .build();

        return chain.filter(exchange.mutate().request(mutateRequest).build());
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Result<?> result = Result.fail(ResponseCodeEnum.UNAUTHORIZED);
        try {
            byte[] bytes = JSONUtils.toBytes(result);
            DataBuffer wrap = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(wrap));
        } catch (Exception e) {
            return response.setComplete();
        }
    }

    private boolean isWhiteListed(String path) {
        for (String pattern : WHITE_LIST) {
            if (PATH_MATCHER.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return -50;
    }
}
