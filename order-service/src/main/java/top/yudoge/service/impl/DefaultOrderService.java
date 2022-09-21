package top.yudoge.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yudoge.clients.ResourceClient;
import top.yudoge.clients.UserClient;
import top.yudoge.exceptions.PayFaildException;
import top.yudoge.pojos.*;
import top.yudoge.repository.impl.BuyOrderRepository;
import top.yudoge.repository.impl.SaleOrderRepository;
import top.yudoge.service.OrderService;

import java.util.Date;

@Service
public class DefaultOrderService implements OrderService {
    @Autowired
    private BuyOrderRepository buyOrderRepository;
    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private UserClient userClient;
    @Autowired
    private ResourceClient resourceClient;


    private <T> T getFromResponseObjectIfSuccessed(ResponseObject responseObject, Class<T> tclz, RuntimeException e) {
        if (responseObject.getCode() != 200) {
            throw e;
        }
        System.out.println(responseObject);
        return (T) responseObject.getData();
    }

    @Override
    public void take(Long uid, Order order) {
        /**
         * 0. 获取资源信息
         * 1. 生成唯一订单ID
         * 2. 获取消费者，重新计算消费者和售出者的coin
         * 3. 生成消费者订单，生成售出者订单
         * 4. 重新设置二者的coin
         * 5. [-] 购买者购买数++，消费者消费数++，资源卖出数++
         * 5. 提交或回滚
         */

        /**
         * order需要有：
         *  {
         *      resource {
         *          id
         *      }
         *      counterPart {
         *          id
         *      }
         *  }
         *  resource{
         *      id
         *  },
         *
         */

        PayFaildException e = new PayFaildException("Pay faild");
        if (uid == null) throw e;

        Resource resource = getFromResponseObjectIfSuccessed(resourceClient.getById(order.getResource().getId()), Resource.class, e);
        String id = new ObjectId().toHexString();
        Long buyerCost = getFromResponseObjectIfSuccessed(userClient.payCoin(uid, resource.getPrice()), Long.class, e);
        Long sallerCost = getFromResponseObjectIfSuccessed(userClient.earnCoin(resource.getPublisherId(), resource.getPrice()), Long.class, e);

        ResourceSnap snap = new ResourceSnap();
        snap.setId(resource.getId());
        snap.setTitle(resource.getTitle());
        snap.setPrice(resource.getPrice());

        Order buyerOrder = new Order();
        buyerOrder.setId(id);
        buyerOrder.setUid(uid);
        buyerOrder.setResource(snap);
        buyerOrder.setRealAmount(buyerCost);
        buyerOrder.setCounterPart(resource.getPublisher());
        buyerOrder.setCreateTime(new Date());

        UserSnap buyer = getFromResponseObjectIfSuccessed(userClient.userSnap(uid), UserSnap.class, e);

        Order sallerOrder = new Order();
        sallerOrder.setId(id);
        sallerOrder.setUid(resource.getPublisherId());
        sallerOrder.setResource(snap);
        sallerOrder.setRealAmount(sallerCost);
        sallerOrder.setCounterPart(buyer);
        buyerOrder.setCreateTime(new Date());

        buyOrderRepository.save(buyerOrder);
        saleOrderRepository.save(sallerOrder);
    }

    @Override
    public void refund(Long uid, Order order) {
    }
}
