package top.yudoge.service;

import top.yudoge.pojos.Order;

public interface OrderService {
    void take(Long uid, Order order);
    void refund(Long uid, String orderId);
}
