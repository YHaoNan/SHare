package top.yudoge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yudoge.constants.Constants;
import top.yudoge.pojos.Resource;
import top.yudoge.pojos.ResourceSearch;
import top.yudoge.pojos.ResponseObject;
import top.yudoge.pojos.ResponseObjectBuilder;
import top.yudoge.service.ResourceService;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;


    /**
     * 根据ID获取资源
     * @param id
     * @return 若系统中有该ID对应的资源，返回该ID对应的资源
     *          否则，返回404状态码
     */
    @GetMapping("/{id}")
    public ResponseObject<Resource, Object> getById(@PathVariable("id") String id) {
        return ResponseObjectBuilder.success(resourceService.getById(id)).build();
    }

    @PutMapping
    public ResponseObject publish(@RequestBody Resource resource,
                                  @RequestHeader(Constants.AUTHENTICATED_UID) Long uid) {
        resourceService.publish(uid, resource);
        return ResponseObjectBuilder.success().build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject delete(@PathVariable("id") String id,
                                 @RequestHeader(Constants.AUTHENTICATED_UID) Long uid) {
        resourceService.delete(uid, id);
        return ResponseObjectBuilder.success().build();
    }

    @PostMapping
    public ResponseObject update(@RequestBody Resource resource,
                                 @RequestHeader(Constants.AUTHENTICATED_UID) Long uid) {
        resourceService.updateById(uid, resource);
        return ResponseObjectBuilder.success().build();
    }

    @PostMapping("/search")
    public ResponseObject search(@RequestBody ResourceSearch search) {
        return ResponseObjectBuilder.success(resourceService.search(search)).build();
    }

}
