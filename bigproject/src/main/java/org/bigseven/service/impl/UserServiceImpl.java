package org.bigseven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.bigseven.entity.User;
import org.bigseven.mapper.UserMapper;
import org.bigseven.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

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
}
