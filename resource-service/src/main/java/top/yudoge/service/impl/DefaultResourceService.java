package top.yudoge.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yudoge.constants.Exchanges;
import top.yudoge.constants.RoutingKeys;
import top.yudoge.exceptions.ResourceNotFoundException;
import top.yudoge.exceptions.UserAuthenticatException;
import top.yudoge.pojos.Resource;
import top.yudoge.pojos.ResourceDoc;
import top.yudoge.repository.ResourceDocRepository;
import top.yudoge.repository.ResourceRepository;
import top.yudoge.service.ResourceService;

import java.util.Optional;
import java.util.function.Supplier;

@Service
public class DefaultResourceService implements ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ResourceDocRepository resourceDocRepository;

    private void requireIsPresentAndIsThePublisher(Long uid, Supplier<Optional<Resource>> supplier) {
        Optional<Resource> resource = supplier.get();
        if (!resource.isPresent())
            throw new ResourceNotFoundException("No this resource!");
        if (resource.get().getPublisherId() != uid)
            throw new UserAuthenticatException("No permission!");
    }


    @Override
    public void publish(Long uid, Resource resource) {
        requireIsPresentAndIsThePublisher(uid, () -> Optional.of(resource));

        resourceRepository.save(resource);
        resourceDocRepository.save(ResourceDoc.fromResource(resource));
        rabbitTemplate.convertAndSend(Exchanges.RESOURCE_TOPIC, RoutingKeys.RESOURCE_PUBLISH_ALL, resource.getId());
    }


    @Override
    public void delete(Long uid, String resourceId) {
        requireIsPresentAndIsThePublisher(uid, () -> resourceRepository.findById(resourceId));

        resourceRepository.deleteById(resourceId);
        resourceDocRepository.deleteById(resourceId);
    }

    @Override
    public void updateById(Long uid, Resource resource) {
        requireIsPresentAndIsThePublisher(uid, () -> resourceRepository.findById(resource.getId().toString()));

        resourceRepository.save(resource);
        resourceDocRepository.save(ResourceDoc.fromResource(resource));
    }


    @Override
    public Resource getById(String id) {
        Optional<Resource> resource = resourceRepository.findById(id);
        if (!resource.isPresent()) throw new ResourceNotFoundException("No resource [" + id + "]");
        return resource.get();
    }

}
