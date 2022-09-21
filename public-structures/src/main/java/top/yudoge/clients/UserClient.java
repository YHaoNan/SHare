package top.yudoge.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import top.yudoge.pojos.ResponseObject;
import top.yudoge.pojos.ResponseObjectBuilder;
import top.yudoge.pojos.UserSnap;

@RequestMapping("/user")
@FeignClient(name = "user-service")
public interface UserClient {
    /**
     * 指定用户支付金币，该方法仅向微服务群提供，不向用户提供
     * @param   uid     要支付的用户id
     * @param   amount  支付金额，不能为负数
     * @return  若操作成功，返回200，并在data里携带实际扣减的金额，若失败，返回对应状态码
     */
    @GetMapping("/payCoin/{id}/{amount}")
    ResponseObject<Long, Object> payCoin(@PathVariable("id") @RequestHeader(name = "authenticated_uid") Long uid, @PathVariable("amount") Long amount);

    /**
     * 指定用户赚取金币，该方法仅向微服务群提供，不向用户提供
     * @param   uid     要支付的用户id
     * @param   amount  赚取金额，不能为负数
     * @return  若操作成功，返回200，并在data里携带实际赚取的金额，若失败，返回对应状态码
     */
    @GetMapping("/earnCoin/{id}/{amount}")
    ResponseObject<Long, Object> earnCoin(@PathVariable("id") @RequestHeader(name = "authenticated_uid") Long uid, @PathVariable("amount") Long amount);

    @GetMapping("/usersnap/{id}")
    ResponseObject<UserSnap, Object> userSnap(@PathVariable("id") Long uid);
}
