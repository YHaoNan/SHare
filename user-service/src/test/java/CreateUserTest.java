import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.yudoge.pojos.User;

public class CreateUserTest {
    public static void main(String[] args) throws JsonProcessingException {
        User user = new User();
        user.setEmail("1355265122@qq.com");
        user.setPassword("123456");
        user.setNick("Yudoge");
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(user));
    }
}
