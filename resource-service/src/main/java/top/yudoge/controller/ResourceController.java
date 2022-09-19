package top.yudoge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yudoge.constants.Constants;
import top.yudoge.exceptions.UserAuthenticatException;
import top.yudoge.pojos.Resource;
import top.yudoge.pojos.ResponseObject;
import top.yudoge.pojos.ResponseObjectBuilder;
import top.yudoge.service.ResourceService;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;


    @GetMapping("/{id}")
    public ResponseObject getById(@PathVariable("id") String id) {
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

}
