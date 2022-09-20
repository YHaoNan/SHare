package top.yudoge.pojos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Directory implements ResourceEntry {

    private String name;
    private Long size;
    private List<ResourceEntry> children;

    public Directory() {
        this.children = new ArrayList<>();
    }

    public Directory(String name, Long size) {
        this(name, size, new ArrayList<>());
    }

    public Directory(String name, Long size, List<ResourceEntry> children) {
        this.name = name;
        this.size = size;
        this.children = children;
    }



    @Override
    public Boolean isDirectory() {
        return Boolean.TRUE;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public Long getSize() {
        return this.size;
    }

    public void setChildren(List<ResourceEntry> children) {
        this.children = children;
    }

    @Override
    public List<ResourceEntry> getChildren() {
        return this.children;
    }

    @Override
    public void addChild(ResourceEntry child) {
        this.children.add(child);
    }

    @Override
    public String toString() {
        return name + "{" + String.join(", ", children.stream().map(r -> r.toString()).collect(Collectors.toList())) + "}";
    }
}
