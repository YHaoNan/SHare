package top.yudoge.pojos;

import lombok.Data;

@Data
public class UserAndPassword {
    private Long id;
    private String email;
    private String password;
}
