package top.yudoge.service;

import top.yudoge.pojos.User;

public interface UserService {
    /**
     * 如果有必要的话，根据传入的token对用户进行认证
     * @param token 用户token
     * @return      如果认证成功，返回该用户的实体，实体中至少应该具有用户id、email和password
     */
    User authentication(String token);
}