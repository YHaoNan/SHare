package top.yudoge.repository.impl;

import org.springframework.stereotype.Repository;

@Repository
public class SaleOrderRepository extends AbstractDocumentBasedOrderRepository {
    public SaleOrderRepository() {
        super("sale_order");
    }
}
