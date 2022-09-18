package top.yudoge.pojos;

import java.util.Iterator;
import java.util.List;

public interface ResourceEntry {
    Boolean isDirectory();
    String getName();
    String size();
    List<ResourceEntry> getChildren();
    void addChild(ResourceEntry child);
}
