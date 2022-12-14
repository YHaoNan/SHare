package top.yudoge.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Data
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@EnableElasticsearchRepositories(basePackages = "top.yudoge.repository")
public class ElasticSearchConfiguration {
    private String host;
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return RestClients.create(
                ClientConfiguration.builder()
                        .connectedTo(host)
                        .build()
        ).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(restHighLevelClient());
    }
}
