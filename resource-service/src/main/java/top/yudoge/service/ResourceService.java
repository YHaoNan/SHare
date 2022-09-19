package top.yudoge.service;

import org.springframework.data.domain.Page;
import top.yudoge.pojos.Resource;
import top.yudoge.pojos.ResourceDoc;
import top.yudoge.pojos.ResourceSearch;

public interface ResourceService {
    void publish(Long uid, Resource resource);
    void delete(Long uid, String resourceId);
    void updateById(Long uid, Resource resource);

    Resource getById(String id);

    Page<Resource> search(ResourceSearch search);
}