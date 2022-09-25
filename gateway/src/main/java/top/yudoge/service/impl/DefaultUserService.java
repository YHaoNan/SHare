package top.yudoge.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.yudoge.pojos.User;
import top.yudoge.pojos.UserAndPassword;
import top.yudoge.repository.UserRepository;
import top.yudoge.service.UserService;
import top.yudoge.utils.AuthenticationUtils;

@Slf4j
@Service
public class DefaultUserService implements UserService, BeanFactoryAware {
    @Autowired
    private UserRepository userRepository;
    private DefaultUserService thisObj;


    @Override
    public UserAndPassword authentication(String token) {
        String email = AuthenticationUtils.getEmail(token);
        if (email == null) return null;
        UserAndPassword user = thisObj.getPasswordByEmail(email);
        return AuthenticationUtils.verifyJWTToken(token, user) ? user : null;
    }

    @Cacheable(cacheNames = "user-auth-cache", key = "#email", sync = true)
    public UserAndPassword getPasswordByEmail(String email) {
        log.info("cache not hit, fetch from db => " + email);
        return userRepository.selectIdEmailAndPasswordByEmail(email);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.thisObj = beanFactory.getBean(DefaultUserService.class);
    }
}
