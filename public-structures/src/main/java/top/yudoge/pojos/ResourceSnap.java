package top.yudoge.pojos;

import lombok.Data;

@Data
public class ResourceSnap {
    private String id;
    private Long price;
    private String title;

    public static ResourceSnap fromResource(Resource resource) {
        ResourceSnap snap = new ResourceSnap();
        snap.setId(resource.getId());
        snap.setPrice(resource.getPrice());
        snap.setTitle(resource.getTitle());
        return snap;
    }
}
