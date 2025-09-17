package org.bigseven.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.bigseven.entity.User;

public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE user_id = #{user_id}")
    User selectByUserId(Integer userId);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);
}
