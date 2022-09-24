package top.yudoge.utils;

import top.yudoge.exceptions.PayFaildException;
import top.yudoge.exceptions.UserNotFoundException;
import top.yudoge.pojos.User;

import java.math.BigDecimal;
import java.math.MathContext;

public class CoinUtils {
    private static final BigDecimal BD_10 = new BigDecimal(10);
    private static final BigDecimal BD_100 = new BigDecimal(100);
    private static final BigDecimal BD_1 = new BigDecimal(1);
    private static final BigDecimal BD_2 = new BigDecimal(2);
    private static final BigDecimal BD_DOT_5 = new BigDecimal(0.5);
    private static final MathContext PRECISION_2 = new MathContext(2);

    public static Long calculateRealCost(User user, Long initialAmount) {
        BigDecimal buyCount = new BigDecimal(user.getBuyCount());
        BigDecimal buyRetCount = new BigDecimal(user.getBuyReturnCount());
        BigDecimal buyFactor;
        if (user.getBuyReturnCount() == 0) buyFactor = BD_1;
        else buyFactor = buyRetCount.divide(buyCount).min(BD_2).round(PRECISION_2);

        BigDecimal price = new BigDecimal(initialAmount).divide(BD_100);
        Long realCostInLong = price.multiply(buyFactor).round(PRECISION_2).multiply(BD_100).longValue();

        return realCostInLong;
    }

    public static void decreaseCoinAndIncreaseBuyCount(User user, Long amount, RuntimeException e) {
        if (user.getCoin() < amount) throw e;
        user.setCoin(user.getCoin() - amount);
        user.setBuyCount(user.getBuyCount() + 1);
    }


    public static Long calculateRealEarn(User user, Long initialAmount) {
        BigDecimal saleCount = new BigDecimal(user.getSaleCount());
        BigDecimal saleRetCount = new BigDecimal(user.getSaleReturnCount());

        BigDecimal saleFactor = null;
        if (user.getSaleCount() == 0) saleFactor = BD_1;
        else saleFactor = BD_1.subtract(saleRetCount.divide(saleCount)).max(BD_DOT_5).round(PRECISION_2);

        BigDecimal price = new BigDecimal(initialAmount).divide(BD_100);
        Long realEarnInLong = price.multiply(saleFactor).round(PRECISION_2).multiply(BD_100).longValue();

        return realEarnInLong;
    }

    public static void increaseCoinAndIncreaseSaleCount(User user, Long amount, RuntimeException e) {
        user.setCoin(user.getCoin() + amount);
        user.setSaleCount(user.getSaleCount() + 1);
    }
}
