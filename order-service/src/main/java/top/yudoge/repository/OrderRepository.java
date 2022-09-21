package top.yudoge.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.yudoge.pojos.Order;

public interface OrderRepository {
    Order save(Order order);
    long deleteById(String id);
    Order getById(String id);
    Page<Order> getByUID(Long uid, Pageable pageable);
}
