package top.yudoge.pojos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
public class Order {
    /**
     * 订单ID，当一个订单创建，会为购买者生成购买订单，为售出者生成售出订单
     * 该ID由当时负责处理的应用程序先行创建，并且，一份交易的购买订单和售出订单的ID相同
     */
    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;
    // 该订单的主用户ID，对于购买者来说，这个ID就是购买者的ID，对于售出者，这个ID就是售出者ID
    private Long uid;
    // 此订单中的资源快照
    private ResourceSnap resource;
    // 用户快照，对于购买者来说，此快照是售出者的快照，对于售出者来说，是购买者的快照
    private UserSnap counterPart;
    // 实际金额，因为存在buyFactor和saleFactor，所以购买消费的金额和售出得到的金额并不一定等于资源价格
    // 而且，通常来说，一份订单中购买者实际花费的金额和售出者实际得到的金额往往不同
    private Long realAmount;
    // 订单创建时间，对于退款订单，则是退款创建实践
    private Date createTime;
}
