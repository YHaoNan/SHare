package top.yudoge.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.yudoge.pojos.User;
import top.yudoge.pojos.UserSnap;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserRepository extends BaseMapper<User> {
    @Select("SELECT id, nick, email FROM tb_user WHERE id=#{id}")
    UserSnap selectUserSnapById(Long id);
    @Select("SELECT email, password FROM tb_user WHERE email=#{email}")
    User selectEmailAndPasswordByEmail(String email);

    @Select("SELECT buy_count, buy_return_count, coin FROM tb_user WHERE id=#{id}")
    User selectBuyAndReturnCountById(Long id);

    @Select("SELECT sale_count, sale_return_count, coin FROM tb_user WHERE id=#{id}")
    User selectSaleAndReturnCountById(Long id);

    @Select("<script>" +
             "SELECT id, nick, email, coin, vip_expire_time, buy_count, buy_return_count, sale_count, sale_return_count FROM tb_user WHERE id IN " +
            "<foreach item=\"id\" collection=\"idset\" open=\"(\" separator=\",\" close=\")\">#{id}</foreach>"+
            "</script>")
    List<User> selectByIdSet(@Param("idset") Set<Long> idset);

    @Update("UPDATE tb_user SET coin = #{coin} WHERE id = #{id}")
    int updateUserCoin(@Param("id") Long id, @Param("coin") Long coin);



}