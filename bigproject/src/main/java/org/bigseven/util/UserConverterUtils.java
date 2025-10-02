package org.bigseven.util;

import org.bigseven.dto.user.UserSimpleVO;
import org.bigseven.entity.User;
import org.springframework.stereotype.Component;

/**
 * UserConverter工具类来将实体转换为 DTO
 *
 * @author shuntianyifang
 * &#064;date 2025/10/2
 */
@Component
public class UserConverterUtils {

    public UserSimpleVO toUserSimpleVO(User user) {
        if (user == null) {
            return null;
        }
        UserSimpleVO vo = new UserSimpleVO();
        vo.setUserId(user.getUserId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setEmail(user.getEmail());
        return vo;
    }
}
