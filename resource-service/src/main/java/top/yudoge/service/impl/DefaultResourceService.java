package top.yudoge.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import top.yudoge.constants.Exchanges;
import top.yudoge.constants.RoutingKeys;
import top.yudoge.exceptions.ResourceNotFoundException;
import top.yudoge.exceptions.UserAuthenticatException;
import top.yudoge.pojos.Resource;
import top.yudoge.pojos.ResourceDoc;
import top.yudoge.pojos.ResourceSearch;
import top.yudoge.repository.ResourceDocRepository;
import top.yudoge.repository.ResourceRepository;
import top.yudoge.service.ResourceService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class DefaultResourceService implements ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ResourceDocRepository resourceDocRepository;
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

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
        resource.setPublishTime(new Date());
        resourceRepository.save(resource);
        resourceDocRepository.save(ResourceDoc.fromResource(resource));

        // 发布消息，只发布id、url和提取码，因为其它的也用不上
        Resource resourceMessage = new Resource();
        resourceMessage.setId(resource.getId());
        resourceMessage.setUrl(resource.getUrl());
        resourceMessage.setCode(resource.getCode());
        rabbitTemplate.convertAndSend(Exchanges.RESOURCE_TOPIC, RoutingKeys.RESOURCE_PUBLISH_ALL, resourceMessage);
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

    @Override
    public Page<Resource> search(ResourceSearch search) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(new MatchQueryBuilder("all", search.getKeyword()));

        if (search.getFromDay() != null)
            queryBuilder.withQuery(new RangeQueryBuilder("publishTime").gte(search.getFromDay()));
        if (search.getToDay() != null)
            queryBuilder.withQuery(new RangeQueryBuilder("publishTime").lte(search.getToDay()));
        if (search.getSizeLowerBound() != null)
            queryBuilder.withQuery(new RangeQueryBuilder("totalSize").gte(search.getSizeLowerBound()));
        if (search.getSizeUpperBound() != null)
            queryBuilder.withQuery(new RangeQueryBuilder("totalSize").gte(search.getSizeUpperBound()));
        if (search.getNetdiskType() != null)
            queryBuilder.withQuery(new TermQueryBuilder("netdiskType", search.getNetdiskType()));

        Pageable pageable = PageRequest.of(search.getOffset(), search.getLimit());

        queryBuilder.withPageable(pageable);

        SearchHits<ResourceDoc> searchHits = elasticsearchRestTemplate.search(queryBuilder.build(), ResourceDoc.class, IndexCoordinates.of("resource"));

        List<Resource> docs = searchHits.getSearchHits().stream().map(
                hit -> ResourceDoc.toResource(hit.getContent())
        ).collect(Collectors.toList());

        return new PageImpl(docs, pageable, searchHits.getTotalHits());
    }

}
