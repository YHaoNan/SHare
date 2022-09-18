package top.yudoge.service;

import org.springframework.data.domain.Page;
import top.yudoge.pojos.Resource;

public interface ResourceService {
    void publish(Resource resource);
    void delete(String resourceId);
    void updateById(Resource resource);
    void offTheShelf(String resourceId);

    Resource getById(String id);
}