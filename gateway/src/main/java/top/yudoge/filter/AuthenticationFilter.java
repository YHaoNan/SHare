package top.yudoge.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.yudoge.constants.Constants;
import top.yudoge.pojos.User;
import top.yudoge.pojos.UserAndPassword;
import top.yudoge.service.UserService;

@Order(-1)
@Component
public class AuthenticationFilter implements GlobalFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationMap authenticationMap;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 如果该请求不需要认证
        if (!authenticationMap.needIntercept(request))
            return chain.filter(exchange);

        // 如果token认证成功
        String token = request.getHeaders().getFirst("authentication");
        if (token != null) {
            try {
                UserAndPassword authenticatedUser = userService.authentication(token);
                if (authenticatedUser != null) {
                    ServerHttpRequest newReq = exchange.getRequest().mutate().header(Constants.AUTHENTICATED_UID, authenticatedUser.getId().toString()).build();
                    return chain.filter(exchange.mutate().request(newReq).build());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 认证失败
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
}
