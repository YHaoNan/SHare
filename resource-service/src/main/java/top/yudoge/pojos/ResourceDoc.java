package top.yudoge.pojos;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import top.yudoge.converter.ResourceEntryToStringConverter;
import top.yudoge.converter.StringToResourceEntryConverter;

import java.util.Date;

@Data
@Document(indexName = "resource")
public class ResourceDoc {
    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Text, copyTo = "all")
    private String title;
    @Field(type = FieldType.Text, copyTo = "all")
    private String bio;
    @Field(type = FieldType.Long)
    private Long price;
    @Field(type = FieldType.Date)
    private Date publishTime;
    // 发布者昵称
    @Field(type = FieldType.Keyword)
    private String publisher;
    @Field(type = FieldType.Date)
    private Date lastCheckTime;
    @Field(type = FieldType.Integer)
    private Integer saleCount;
    @Field(type = FieldType.Integer)
    private Integer refundCount;
    @Field(type = FieldType.Text, copyTo = "all")
    private String directory;
    @Field(type = FieldType.Text)
    private String all;
    @Field(type = FieldType.Long)
    private Long totalSize;

    @Field(type = FieldType.Integer)
    private Integer netdiskType;

    public ResourceDoc() {}

    /**
     *     private ObjectId id;
     *     private String url;
     *     private String code;
     *     private String title;
     *     private String bio;
     *     private Long price;
     *     private Date publishTime;
     *     private Long publisherId;
     *     private UserSnap publisher;
     *     private Date lastCheckTime;
     *     private Boolean checkPending;
     *     private ResourceState state;
     *     private Integer saleCount;
     *     private Integer refundCount;
     *     private ResourceEntry directory;
     *     private Long totalSize;
     *     private Integer netdiskType;
     */
    public static ResourceDoc fromResource(Resource resource) {
        ResourceDoc doc = new ResourceDoc();
        doc.id = resource.getId();
        doc.title = resource.getTitle();
        doc.bio = resource.getBio();
        doc.price = resource.getPrice();
        doc.publishTime = resource.getPublishTime();
        if (resource.getPublisher() != null)
            doc.publisher = resource.getPublisher().getNick();
        doc.lastCheckTime = resource.getLastCheckTime();
        doc.saleCount = resource.getSaleCount();
        doc.refundCount = resource.getRefundCount();
        if (resource.getDirectory() != null)
            doc.directory = new ResourceEntryToStringConverter().convert(resource.getDirectory());
        doc.totalSize = resource.getTotalSize();
        doc.netdiskType = resource.getNetdiskType();
        return doc;
    }

    public static Resource toResource(ResourceDoc doc) {
        Resource resource = new Resource();
        resource.setId(doc.getId());
        resource.setTitle(doc.getTitle());
        resource.setBio(doc.getBio());
        resource.setPrice(doc.getPrice());
        resource.setPublishTime(doc.getPublishTime());
        UserSnap snap = new UserSnap();
        snap.setNick(doc.getPublisher());
        resource.setPublisher(snap);
        resource.setLastCheckTime(doc.getLastCheckTime());
        resource.setSaleCount(doc.getSaleCount());
        resource.setRefundCount(doc.getRefundCount());
        if (doc.getDirectory() != null)
            resource.setDirectory(new StringToResourceEntryConverter().convert(doc.getDirectory()));
        resource.setTotalSize(doc.getTotalSize());
        resource.setNetdiskType(doc.getNetdiskType());
        return resource;
    }

}
