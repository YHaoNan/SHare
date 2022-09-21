package top.yudoge.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yudoge.pojos.Order;
import top.yudoge.pojos.ResponseObject;
import top.yudoge.pojos.ResponseObjectBuilder;
import top.yudoge.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PutMapping
    public ResponseObject takeOrder(@RequestBody Order order,
                                    @RequestHeader("authenticated_uid") Long uid) {
        orderService.take(uid, order);
        return ResponseObjectBuilder.success().build();
    }
}
