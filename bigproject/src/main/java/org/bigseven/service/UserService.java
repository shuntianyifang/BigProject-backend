package org.bigseven.service;

import org.bigseven.constant.UserTypeEnum;

public interface UserService {

    Integer login(String username, String password, String email);

    Integer register(String username, String password, String email, UserTypeEnum userType);
}
