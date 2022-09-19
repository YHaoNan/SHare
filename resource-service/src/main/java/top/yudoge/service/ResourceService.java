package top.yudoge.service;

import org.springframework.data.domain.Page;
import top.yudoge.pojos.Resource;

public interface ResourceService {
    void publish(Long uid, Resource resource);
    void delete(Long uid, String resourceId);
    void updateById(Long uid, Resource resource);

    Resource getById(String id);
}