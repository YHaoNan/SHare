package top.yudoge.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
public class AuthenticationMapTest {
    @Autowired
    private AuthenticationMap authenticationMap;
    @Autowired
    Environment env;

    @Test
    void testAuthenticationWhiteListInjection() {
        System.out.println(authenticationMap.getWhiteList());
    }
}
