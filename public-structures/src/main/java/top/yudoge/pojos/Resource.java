package top.yudoge.pojos;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import top.yudoge.constants.ResourceState;

import java.math.BigInteger;
import java.util.Date;

/**
 * Resource代表用户发表的一个资源
 */
@Data
@Document("resource")
public class Resource {
    @MongoId
    private ObjectId id;
    private String url;
    private String code;
    private String title;
    private String bio;
    private Long price;
    private Date publishTime;
    private Long publisherId;
    private UserSnap publisher;
    private Date lastCheckTime;
    private Boolean checkPending;
    private ResourceState state;
    private Integer saleCount;
    private Integer refundCount;
    private ResourceEntry directory;
}
