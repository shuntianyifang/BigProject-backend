package org.bigseven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.entity.User;
import org.bigseven.mapper.UserMapper;
import org.bigseven.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    /**
     * @Description:这个方法才是用户登录,需要重写
     * @Author: BigSeven
     */
    @Override
    public Integer login(String username, String password, String email) {
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(userQueryWrapper);
        if(user == null){
            user = User.builder()
                    .username(username)
                    .password(password)
                    .build();
            userMapper.insert(user);
        }
        else{
            if(!user.getPassword().equals(password)){
                //需要做统一错误处理,这里只是暂时先写-1
                return -1;
            }
        }
        return user.getUserId();
    }
    /**
    * @Description:这个方法才是用户注册,login方法未来要改成登录用
    * @Author: BigSeven
    */
    @Override
    public Integer register(String username, String password, String email, UserTypeEnum userType) {
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(userQueryWrapper);
        // 如果用户不存在，则创建并保存新用户
        if (user == null) {
            // 创建新用户对象
            user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .userType(userType)
                    .build();

            // 插入新用户到数据库
            userMapper.insert(user);


        } else {
            // 如果用户已经存在，返回-1或其他错误码
            // 这里假设-1表示用户名已经存在
            return -1;
        }
        return user.getUserId();

    }
}
