package top.yudoge.pojos;

import java.util.List;

public class File implements ResourceEntry {

    private String name;
    private Long size;

    public File() {}
    public File(String name, Long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public Boolean isDirectory() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public Long getSize() {
        return this.size;
    }

    @Override
    public List<ResourceEntry> getChildren() {
        return null;
    }

    @Override
    public void addChild(ResourceEntry child) {
        throw new IllegalStateException("You can not add a child to file");
    }

    @Override
    public String toString() {
        return name;
    }
}
