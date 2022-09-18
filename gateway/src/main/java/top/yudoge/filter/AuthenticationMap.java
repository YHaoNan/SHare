package top.yudoge.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.server.reactive.ServerHttpRequest;
import top.yudoge.utils.YamlPropertySourceFactory;

import java.util.List;

@Data
@Configuration
@PropertySource(value = {"classpath:authentication.yaml"}, factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "authentication")
public class AuthenticationMap {
    private List<String> whiteList;

    public boolean needIntercept(ServerHttpRequest request) {
        String requestPath = request.getPath().toString();
        String requestMethod = request.getMethod().name().toUpperCase();

        for (String methodAndPattern : whiteList) {
            int splitorPos = methodAndPattern.indexOf(":");
            String method = methodAndPattern.substring(0, splitorPos);
            String pattern = methodAndPattern.substring(splitorPos + 1);
            if (method.equals(requestMethod) && requestPath.matches(pattern)) {
                return false;
            }
        }
        return true;
    }

}
