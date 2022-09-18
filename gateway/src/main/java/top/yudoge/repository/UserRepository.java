package top.yudoge.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.yudoge.pojos.User;

@Mapper
public interface UserRepository extends BaseMapper<User> {
    /**
     * 该方法根据邮箱检索用户，并且查出用户的id、email以及password
     * @return
     */
    @Select("SELECT id, email, password FROM tb_user WHERE email=#{email}")
    User selectIdEmailAndPasswordByEmail(String email);

}