package org.bigseven.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.bigseven.entity.User;

/**
 * 用户映射器
 *
 * @author v185v
 * &#064;date 2025/9/16
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户信息
     * 通过用户名精确匹配查询用户表中的记录
     *
     * @param username 用户登录名
     * @return 对应的用户实体对象，如果不存在则返回null
     */
    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);
}
