package top.yudoge.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yudoge.clients.ResourceClient;
import top.yudoge.clients.UserClient;
import top.yudoge.exceptions.PayFaildException;
import top.yudoge.exceptions.RefundFaildException;
import top.yudoge.pojos.*;
import top.yudoge.repository.impl.BuyOrderRepository;
import top.yudoge.repository.impl.SaleOrderRepository;
import top.yudoge.service.OrderService;
import top.yudoge.utils.CoinUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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


    private <T> T getFromResponseObjectIfSuccessed(ResponseObject responseObject, RuntimeException e) {
        if (responseObject.getCode() != 200) {
            throw e;
        }
        System.out.println(responseObject);
        return (T) responseObject.getData();
    }


    private void requireFirstNthElementNotNull(Collection c, int count, RuntimeException e) {
        if (c.size() < count) throw e;
        int counter = 0;
        for (Object o : c) {
            if (counter == count) break;
            if (o == null) throw e;
            counter++;
        }
    }

    @Override
    public void take(Long uid, Order order) {
        /**
         * 0. 获取资源信息                                 1
         * 1. 获取购买者信息                               2    1，2可以捏成一步
         * 2. 获取售出者信息                               3
         * 3. 生成唯一订单ID
         * 4. 重新计算消费者和售出者的coin
         * 5. 生成消费者订单                              4
         * 6. 生成售出者订单                              5
         * 7. 重新设置交易双方的coin、buyCount、saleCount   6    捏成一步
         * 8. 重新设置资源卖出数                           7
         * 9. 提交或回滚
         */


        PayFaildException e = new PayFaildException("Pay faild");
        if (uid == null) throw e;


        // 获取资源
        Resource resource = getFromResponseObjectIfSuccessed(resourceClient.getById(order.getResource().getId()), e);

        // 获取购买者和销售者
        List<User> participants = getFromResponseObjectIfSuccessed(userClient.getByIdSet(uid + "," + resource.getPublisherId()), e);
        requireFirstNthElementNotNull(participants, 2, e);

        int buyerIdx = participants.get(0).getId() == uid ? 0 : 1;
        int sallerIdx = participants.get(0).getId() == uid ? 1 : 0;
        User buyer = participants.get(buyerIdx);
        User saller = participants.get(sallerIdx);

        // 生成订单id
        String id = new ObjectId().toHexString();

        // 计算并设置实际的花费和赚取金额以及salecount和buycount
        Long buyerRealCost = CoinUtils.calculateRealCost(buyer, resource.getPrice());
        Long sallerRealEarn = CoinUtils.calculateRealEarn(saller, resource.getPrice());
        CoinUtils.decreaseCoinAndIncreaseBuyCount(buyer, buyerRealCost, e);
        CoinUtils.increaseCoinAndIncreaseSaleCount(saller, sallerRealEarn, e);

        // 生成资源快照，用于稍后创建订单
        ResourceSnap resourceSnap = ResourceSnap.fromResource(resource);

        // 创建订单
        Order buyerOrder = new Order();
        buyerOrder.setId(id);
        buyerOrder.setUid(uid);
        buyerOrder.setResource(resourceSnap);
        buyerOrder.setRealAmount(buyerRealCost);
        buyerOrder.setCounterPart(UserSnap.fromUser(saller));
        buyerOrder.setCreateTime(new Date());

        Order sallerOrder = new Order();
        sallerOrder.setId(id);
        sallerOrder.setUid(resource.getPublisherId());
        sallerOrder.setResource(resourceSnap);
        sallerOrder.setRealAmount(sallerRealEarn);
        sallerOrder.setCounterPart(UserSnap.fromUser(buyer));
        buyerOrder.setCreateTime(new Date());

        buyOrderRepository.save(buyerOrder);
        saleOrderRepository.save(sallerOrder);

        // 更新资源卖出数
        resource.setSaleCount(resource.getSaleCount() + 1);
        resourceClient.update(resource, resource.getPublisherId());

        // 更新用户
        userClient.update(buyer, buyer.getId());
        userClient.update(saller, saller.getId());
    }

    @Override
    public void refund(Long uid, String orderId) {
        /**
         * 1. 获取交易双方
         * 2. 获取交易双方订单
         * 3. 获取交易资源
         * 4. 修改双方退款数和coin
         * 5. 删除双方订单，并添加到退款订单中(暂不实现退款订单)
         * 6. 修改资源退款数
         */

        RefundFaildException e = new RefundFaildException("Refund faild");

        Order buyerOrder = buyOrderRepository.getById(orderId);
        Order sallerOrder = saleOrderRepository.getById(orderId);

        List<User> participants = getFromResponseObjectIfSuccessed(userClient.getByIdSet(buyerOrder.getUid() + "," + sallerOrder.getUid()), e);
        requireFirstNthElementNotNull(participants, 2, e);
        int buyerIdx = participants.get(0).getId() == uid ? 0 : 1;
        int sallerIdx = participants.get(0).getId() == uid ? 1 : 0;

        User buyer = participants.get(buyerIdx);
        User saller = participants.get(sallerIdx);
        Resource resource = getFromResponseObjectIfSuccessed(resourceClient.getById(buyerOrder.getResource().getId()), e);

        resource.setRefundCount(resource.getRefundCount() + 1);
        resourceClient.update(resource, resource.getPublisherId());

        buyer.setBuyReturnCount(buyer.getBuyReturnCount() + 1);
        saller.setSaleReturnCount(saller.getSaleReturnCount() + 1);
        buyer.setCoin(buyer.getCoin() + buyerOrder.getRealAmount());
        // 这里可能出现负数，毕方铺的处理策略是余额可以出现负数，这里是归零，反正虚拟货币
        saller.setCoin(Math.max(saller.getCoin() - buyerOrder.getRealAmount(), 0));

        userClient.update(buyer, buyer.getId());
        userClient.update(saller, saller.getId());

        buyOrderRepository.deleteById(buyerOrder.getId());
        saleOrderRepository.deleteById(sallerOrder.getId());

    }
}
