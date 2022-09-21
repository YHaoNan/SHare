package top.yudoge.service;

import top.yudoge.pojos.User;
import top.yudoge.pojos.UserSnap;

public interface UserService {
    /**
     * 注册
     * @param user
     * @throws 数据库抛出的异常
     */
    void register(User user);

    /**
     * 认证，或者说登录。传入username和password，若与数据库数据匹配，则生成一个认证token返回
     * @param email     邮箱
     * @param password  密码
     * @throws top.yudoge.exceptions.UserNotFoundException 当email对应的用户并不存在时
     * @throws top.yudoge.exceptions.UserAuthenticatException 当email和密码不匹配导致认证失败时
     * @throws top.yudoge.exceptions.ServerInternalException 当颁发token时发生异常
     * @return token
     */
    String authentication(String email, String password);

    /**
     * 返回具有指定ID的User，若没有返回null
     * @param id
     * @return
     */
    User getUser(Long id);

    /**
     * 根据ID更新User
     * @param user
     * @throws 数据库抛出的异常
     */
    void update(User user);

    /**
     * 用户支付金币
     * @param uid     支付用户
     * @param amount  支付金额
     * @throws top.yudoge.exceptions.UserNotFoundException 如果用户不存在
     * @throws PayFaildException 如果支付失败
     * @return 成功返回实际支付额，失败抛出PayFaildException
     */
    Long payCoin(Long uid, Long amount);

    /**
     * 用户赚取金币
     * @param uid     赚取用户
     * @param amount  赚取金额
     * @throws top.yudoge.exceptions.UserNotFoundException 如果用户不存在
     * @return 成功返回实际赚取额，失败抛出EarnFaildException
     */
    Long earnCoin(Long uid, Long amount);

    UserSnap getUserSnapById(Long uid);

}