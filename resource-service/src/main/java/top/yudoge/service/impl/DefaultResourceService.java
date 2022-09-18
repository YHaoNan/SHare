package top.yudoge.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yudoge.constants.Exchanges;
import top.yudoge.constants.RoutingKeys;
import top.yudoge.exceptions.ResourceNotFoundException;
import top.yudoge.pojos.Resource;
import top.yudoge.pojos.ResourceDoc;
import top.yudoge.repository.ResourceDocRepository;
import top.yudoge.repository.ResourceRepository;
import top.yudoge.service.ResourceService;

import java.util.Optional;

@Service
public class DefaultResourceService implements ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ResourceDocRepository resourceDocRepository;

    @Override
    public void publish(Resource resource) {
        resourceRepository.save(resource);
        resourceDocRepository.save(ResourceDoc.fromResource(resource));
        rabbitTemplate.convertAndSend(Exchanges.RESOURCE_TOPIC, RoutingKeys.RESOURCE_PUBLISH_ALL, resource.getId());
    }

    @Override
    public void delete(String resourceId) {
        resourceRepository.deleteById(resourceId);
        resourceDocRepository.deleteById(resourceId);
    }

    @Override
    public void updateById(Resource resource) {
        resourceRepository.save(resource);
        resourceDocRepository.save(ResourceDoc.fromResource(resource));
    }

    @Override
    public void offTheShelf(String resourceId) {
        // N/A
    }

    @Override
    public Resource getById(String id) {
        Optional<Resource> resource = resourceRepository.findById(id);
        if (!resource.isPresent()) throw new ResourceNotFoundException("No resource [" + id + "]");
        return resource.get();
    }

}
