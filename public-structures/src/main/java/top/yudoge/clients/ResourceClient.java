package top.yudoge.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.yudoge.pojos.Resource;
import top.yudoge.pojos.ResponseObject;
import top.yudoge.pojos.ResponseObjectBuilder;

@FeignClient("resource-service")
@RequestMapping("/resource")
public interface ResourceClient {
    /**
     * 根据ID获取资源
     * @param id
     * @return 若系统中有该ID对应的资源，返回该ID对应的资源
     *          否则，返回404状态码
     */
    @GetMapping("/{id}")
    ResponseObject<Resource, Object> getById(@PathVariable("id") String id);
}
