package top.yudoge.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.yudoge.constants.Exchanges;
import top.yudoge.constants.Queues;
import top.yudoge.constants.RoutingKeys;

@Configuration
public class AMQPConfig {
    @Bean
    public Exchange resourceExchange() {
        return ExchangeBuilder.topicExchange(Exchanges.RESOURCE_TOPIC).durable(true).build();
    }

    @Bean
    public Queue resourceCheckQueue() {
        return QueueBuilder.durable(Queues.RESOURCE_CHECK).build();
    }

    @Bean
    public Queue resourceEmailNotifyQueue() {
        return QueueBuilder.durable(Queues.RESOURCE_EMAIL_NOTIFY).build();
    }

    @Bean
    public Binding resourcePublishCheckBinding() {
        return BindingBuilder.bind(resourceCheckQueue()).to(resourceExchange()).with(RoutingKeys.RESOURCE_PUBLISH_CHECK).noargs();
    }
    @Bean
    public Binding resourcePublishEmailNotifyBinding() {
        return BindingBuilder.bind(resourceEmailNotifyQueue()).to(resourceExchange()).with(RoutingKeys.RESOURCE_PUBLISH_EMAIL_NOTIFY).noargs();
    }

}
