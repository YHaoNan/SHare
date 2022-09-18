package top.yudoge.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.yudoge.pojos.Resource;

@Repository
public interface ResourceRepository extends MongoRepository<Resource, String> {
}
