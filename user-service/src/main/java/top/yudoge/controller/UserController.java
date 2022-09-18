package top.yudoge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yudoge.pojos.ResponseObject;
import top.yudoge.pojos.ResponseObjectBuilder;
import top.yudoge.pojos.User;
import top.yudoge.service.UserService;

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
    public ResponseObject update(@RequestBody User user) {
        userService.update(user);
        return ResponseObjectBuilder.success().build();
    }


    @GetMapping("/{id}")
    public ResponseObject getUser(@PathVariable("id") Long id) {
        return ResponseObjectBuilder.success(
                userService.getUser(id)
        ).build();
    }

}
