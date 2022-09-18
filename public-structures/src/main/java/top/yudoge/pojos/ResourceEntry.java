package top.yudoge.pojos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import top.yudoge.converter.ResourceEntryDeserializer;

import java.util.Iterator;
import java.util.List;

@JsonDeserialize(using = ResourceEntryDeserializer.class)
public interface ResourceEntry {
    Boolean isDirectory();
    String getName();
    void setName(String name);
    String getSize();
    void setSize(String size);
    List<ResourceEntry> getChildren();
    void addChild(ResourceEntry child);
}
