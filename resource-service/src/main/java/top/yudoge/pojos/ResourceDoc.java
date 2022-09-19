package top.yudoge.pojos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import top.yudoge.converter.ResourceEntryToStringConverter;

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

    public ResourceDoc() {}

    public static ResourceDoc fromResource(Resource resource) {
        ResourceDoc doc = new ResourceDoc();
        doc.id = resource.getId().toString();
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
        return doc;
    }

}
