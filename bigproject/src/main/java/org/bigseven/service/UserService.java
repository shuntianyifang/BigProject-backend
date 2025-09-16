package org.bigseven.service;

import org.bigseven.constant.UserTypeEnum;

public interface UserService {
    public Integer login(String username, String password, String email);
    public Integer register(String username, String password, String email, UserTypeEnum userType);
}
