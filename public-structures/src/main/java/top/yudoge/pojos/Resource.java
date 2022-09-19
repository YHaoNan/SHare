package top.yudoge.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.internal.NotNull;
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
    private String id;
    private String url;
    private String code;
    private String title;
    private String bio;
    private Long price;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date publishTime;
    private Long publisherId;
    private UserSnap publisher;
    private Date lastCheckTime;
    private Boolean checkPending;
    private ResourceState state;
    private Integer saleCount;
    private Integer refundCount;
    private ResourceEntry directory;
    /**
     * 文件大小，单位Byte
     * 该字段最大能表示8192PB的文件大小，已经足够，若超出，则应该设置成Long.MAX_VALUE
     * 8192PB
     */
    private Long totalSize;
    private Integer netdiskType;



}
