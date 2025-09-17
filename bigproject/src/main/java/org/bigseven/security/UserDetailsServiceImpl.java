package org.bigseven.security;

import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.UserTypeEnum;
import org.bigseven.entity.User;
import org.bigseven.exception.ApiException;
import org.bigseven.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userMapper.selectByUsername(username);

        if (user == null) {
            throw new ApiException(ExceptionEnum.USER_NOT_EXIST);
        }

        Collection<? extends GrantedAuthority> authorities = getAuthorities(user.getUserType());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserTypeEnum userType) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        switch (userType) {
            case SUPER_ADMIN:
                authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("SYSTEM_MANAGE"));
                break;
            case ADMIN:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("POST_MANAGE"));
                authorities.add(new SimpleGrantedAuthority("USER_MANAGE"));
                break;
            case STUDENT:
                authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
                authorities.add(new SimpleGrantedAuthority("POST_CREATE"));
                authorities.add(new SimpleGrantedAuthority("POST_READ"));
                break;
            default:
                break;
        }

        return authorities;
    }
}
