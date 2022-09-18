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
@Transactional
public class DefaultUserService implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public void register(User user) {
        user.setPassword(
                AuthenticationUtils.saltHash(user.getPassword())
        );
        userRepository.insert(user);
    }

    @Override
    public String authentication(String email, String password) {
        User user = userRepository.selectEmailAndPasswordByEmail(email);
        if (user == null)
            throw new UserNotFoundException("Can't find user by email : " + email);

        String hashedPassword = AuthenticationUtils.saltHash(password);
        if (!hashedPassword.equals(user.getPassword()))
            throw new UserAuthenticatException("Email and password mismatch");

        try {
            return AuthenticationUtils.generateJWTToken(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerInternalException("An error occurd when we sign a token");
        }
    }

    @Override
    public User getUser(Long id) {
        return userRepository.selectById(id);
    }

    @Override
    public void update(User user) {
        userRepository.updateById(user);
    }
}
