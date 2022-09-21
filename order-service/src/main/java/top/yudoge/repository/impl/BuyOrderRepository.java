package top.yudoge.repository.impl;

import org.springframework.stereotype.Repository;

@Repository
public class BuyOrderRepository extends AbstractDocumentBasedOrderRepository {
    public BuyOrderRepository() {
        super("buy_order");
    }
}
