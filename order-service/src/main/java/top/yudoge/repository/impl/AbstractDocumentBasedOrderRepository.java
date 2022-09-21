package top.yudoge.repository.impl;


import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import top.yudoge.pojos.Order;
import top.yudoge.repository.OrderRepository;

import java.util.List;

public abstract class AbstractDocumentBasedOrderRepository implements OrderRepository {
    @Autowired
    private MongoTemplate mongoTemplate;
    private String collectionName;
    public AbstractDocumentBasedOrderRepository(String collectionName) {
        this.collectionName = collectionName;
    }


    @Override
    public Order getById(String id) {
        return mongoTemplate.findById(id, Order.class, collectionName);
    }

    @Override
    public Page<Order> getByUID(Long uid, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("uid").is(uid));
        query.with(pageable);
        List<Order> orderList = mongoTemplate.find(query, Order.class, collectionName);
        return PageableExecutionUtils.getPage(
                orderList, pageable, () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Order.class, collectionName)
        );
    }

    @Override
    public Order save(Order order) {
        return mongoTemplate.save(order, collectionName);
    }

    @Override
    public long deleteById(String id) {
        DeleteResult deleteResult = mongoTemplate.remove(
                new Query().addCriteria(Criteria.where("_id").is(id)),
                collectionName
        );
        return deleteResult.getDeletedCount();
    }
}
