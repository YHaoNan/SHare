package top.yudoge.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.yudoge.pojos.ResourceDoc;

@Repository
public interface ResourceDocRepository extends ElasticsearchRepository<ResourceDoc, String> {

}
