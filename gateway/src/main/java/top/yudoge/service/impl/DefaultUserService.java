package top.yudoge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yudoge.exceptions.ServerInternalException;
import top.yudoge.exceptions.UserAuthenticatException;
import top.yudoge.exceptions.UserNotFoundException;
import top.yudoge.pojos.User;
import top.yudoge.repository.UserRepository;
import top.yudoge.service.UserService;
import top.yudoge.utils.AuthenticationUtils;

@Service
public class DefaultUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User authentication(String token) {
        String email = AuthenticationUtils.getEmail(token);
        User user = userRepository.selectIdEmailAndPasswordByEmail(email);
        return AuthenticationUtils.verifyJWTToken(token, user) ? user : null;
    }

}
