package top.yudoge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.util.resources.cldr.ig.LocaleNames_ig;
import top.yudoge.exceptions.PayFaildException;
import top.yudoge.exceptions.ServerInternalException;
import top.yudoge.exceptions.UserAuthenticatException;
import top.yudoge.exceptions.UserNotFoundException;
import top.yudoge.pojos.User;
import top.yudoge.pojos.UserSnap;
import top.yudoge.repository.UserRepository;
import top.yudoge.service.UserService;
import top.yudoge.utils.AuthenticationUtils;

import java.math.BigDecimal;
import java.math.MathContext;

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
        User user = userRepository.selectById(id);
        if (user == null) throw new UserNotFoundException("No user [id=" + id +"]");
        return user;
    }

    @Override
    public void update(User user) {
        userRepository.updateById(user);
    }

    private static final BigDecimal BD_10 = new BigDecimal(10);
    private static final BigDecimal BD_100 = new BigDecimal(100);
    private static final BigDecimal BD_1 = new BigDecimal(1);
    private static final BigDecimal BD_2 = new BigDecimal(2);
    private static final BigDecimal BD_DOT_5 = new BigDecimal(0.5);
    private static final MathContext PRECISION_2 = new MathContext(2);
    @Override
    public Long payCoin(Long uid, Long amount) {
        User user = userRepository.selectBuyAndReturnCountById(uid);
        if (user == null) throw new UserNotFoundException("No user [id=" + uid +"]");
        BigDecimal buyCount = new BigDecimal(user.getBuyCount());
        BigDecimal buyRetCount = new BigDecimal(user.getBuyReturnCount());
        BigDecimal buyFactor = null;
        if (user.getBuyReturnCount() == 0) buyFactor = BD_1;
        else buyFactor = buyRetCount.divide(buyCount).min(BD_2).round(PRECISION_2);

        BigDecimal price = new BigDecimal(amount).divide(BD_100);
        Long realCostInLong = price.multiply(buyFactor).round(PRECISION_2).multiply(BD_100).longValue();

        if (user.getCoin() < realCostInLong) throw new PayFaildException("Insufficient balance");

        userRepository.updateUserCoin(uid, user.getCoin() - realCostInLong);
        return realCostInLong;
    }

    @Override
    public Long earnCoin(Long uid, Long amount) {
        User user = userRepository.selectSaleAndReturnCountById(uid);
        if (user == null) throw new UserNotFoundException("No user [id=" + uid +"]");
        BigDecimal saleCount = new BigDecimal(user.getSaleCount());
        BigDecimal saleRetCount = new BigDecimal(user.getSaleReturnCount());

        BigDecimal saleFactor = null;
        if (user.getSaleCount() == 0) saleFactor = BD_1;
        else saleFactor = BD_1.subtract(saleRetCount.divide(saleCount)).max(BD_DOT_5).round(PRECISION_2);

        BigDecimal price = new BigDecimal(amount).divide(BD_100);
        Long realEarnInLong = price.multiply(saleFactor).round(PRECISION_2).multiply(BD_100).longValue();

        userRepository.updateUserCoin(uid, user.getCoin() + realEarnInLong);
        return realEarnInLong;
    }

    @Override
    public UserSnap getUserSnapById(Long uid) {
        UserSnap snap = userRepository.selectUserSnapById(uid);
        if (snap == null) throw new UserNotFoundException("User not found");
        return snap;
    }
}
