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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;



/**
 * 用户详情服务实现类
 *
 * @author shuntianyifang
 * &#064;date 2025/9/17
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    public JwtUserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 根据用户名加载用户详情
     *
     * @param username 用户名
     * @return UserDetails 用户详细信息对象
     * @throws ApiException 当用户不存在时抛出异常
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {

        User user = userMapper.selectByUsername(username);

        if (user == null) {
            throw new ApiException(ExceptionEnum.USER_NOT_EXIST);
        }
        ///获取用户权限集合
        Collection<? extends GrantedAuthority> authorities = getAuthorities(user.getUserType());

        return new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }

    /// 为对应的角色授予不同的权限
    /**
     * 根据用户类型获取对应的权限集合
     *
     * @param userType 用户类型枚举值，用于确定用户的角色和权限
     * @return 返回该用户类型对应的所有权限集合，包括角色权限和具体操作权限
     */
    private Collection<? extends GrantedAuthority> getAuthorities(UserTypeEnum userType) {
        Set<GrantedAuthority> authorities = new HashSet<>(32);

        /// 根据不同的用户类型分配相应的角色和权限
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
