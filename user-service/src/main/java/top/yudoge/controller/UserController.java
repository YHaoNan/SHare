package top.yudoge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yudoge.constants.Constants;
import top.yudoge.exceptions.UserAuthenticatException;
import top.yudoge.pojos.ResponseObject;
import top.yudoge.pojos.ResponseObjectBuilder;
import top.yudoge.pojos.User;
import top.yudoge.service.UserService;

import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseObject login(@RequestBody User user) {
        String token = userService.authentication(user.getEmail(), user.getPassword());
        return ResponseObjectBuilder.success()
                .dataWithMap().addData("token", token)
                .build();
    }

    @PutMapping
    public ResponseObject register(@RequestBody User user) {
        userService.register(user);
        return ResponseObjectBuilder.success().build();
    }

    @PostMapping
    public ResponseObject update(@RequestBody User user, @RequestHeader(Constants.AUTHENTICATED_UID) Long currentUID) {
        if (currentUID == user.getId())
            userService.update(user);
        else throw new UserAuthenticatException("You can only update your self.");
        return ResponseObjectBuilder.success().build();
    }


    @GetMapping("/{id}")
    public ResponseObject getUser(@PathVariable("id") Long id) {
        return ResponseObjectBuilder.success(
                userService.getUser(id)
        ).build();
    }

//    /**
//     * 指定用户支付金币，该方法仅向微服务群提供，不向用户提供
//     * @param   uid     要支付的用户id
//     * @param   amount  支付金额，不能为负数
//     * @return  若操作成功，返回200，并在data里携带实际扣减的金额，若失败，返回对应状态码
//     */
//    @GetMapping("/payCoin/{id}/{amount}")
//    public ResponseObject payCoin(@PathVariable("id") Long uid, @PathVariable("amount") Long amount) {
//        return ResponseObjectBuilder.success(userService.payCoin(uid, amount)).build();
//    }
//
//    /**
//     * 指定用户赚取金币，该方法仅向微服务群提供，不向用户提供
//     * @param   uid     要支付的用户id
//     * @param   amount  赚取金额，不能为负数
//     * @return  若操作成功，返回200，并在data里携带实际赚取的金额，若失败，返回对应状态码
//     */
//    @GetMapping("/earnCoin/{id}/{amount}")
//    public ResponseObject earnCoin(@PathVariable("id") Long uid, @PathVariable("amount") Long amount) {
//        return ResponseObjectBuilder.success(userService.earnCoin(uid, amount)).build();
//    }
//
//    @GetMapping("/usersnap/{id}")
//    public ResponseObject userSnap(@PathVariable("id") Long uid) {
//        return ResponseObjectBuilder.success(userService.getUser(uid)).build();
//    }

    @GetMapping("/byset/{ids}")
    public ResponseObject getByIdSet(@PathVariable("ids") String ids) {
        String idArr[] = ids.split(",");
        Set<Long> idset = new TreeSet<>();
        for (String id : idArr) {
            idset.add(Long.parseLong(id));
        }

        return ResponseObjectBuilder.success(userService.getByIdSet(idset)).build();
    }

}
