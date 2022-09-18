package top.yudoge.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.yudoge.pojos.User;

@Mapper
public interface UserRepository extends BaseMapper<User> {
    /**
     * 该方法
     * @return
     */
    @Select("SELECT email, password FROM tb_user WHERE email=#{email}")
    User selectEmailAndPasswordByEmail(String email);

}