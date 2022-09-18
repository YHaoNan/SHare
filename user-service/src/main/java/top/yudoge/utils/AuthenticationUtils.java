package top.yudoge.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.util.DigestUtils;
import top.yudoge.pojos.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AuthenticationUtils {

    private static final int EXPIRE_DAYS = 7;
    private static final String SALT = "PLFA42tji#TR dcvA 1#JRa j013 Vj9 v239";
    public static String saltHash(String source) {
        String saltedString = source + SALT;
        return DigestUtils.md5DigestAsHex(saltedString.getBytes());
    }



    public static String generateJWTToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(user.getPassword());
        return JWT.create()
                .withClaim("email", user.getEmail())
                .withExpiresAt(Date.from(
                        LocalDateTime.now().plusDays(EXPIRE_DAYS)
                                .atZone(ZoneId.systemDefault()).toInstant()
                ))
                .sign(algorithm);
    }

    public static boolean verifyJWTToken(String token, User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(user.getPassword());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("email", user.getEmail())
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
