package top.yudoge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import top.yudoge.clients.ResourceClient;
import top.yudoge.clients.UserClient;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(clients = {UserClient.class, ResourceClient.class})
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
