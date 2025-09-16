package org.bigseven.controller;


import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.bigseven.dto.UserLoginRequest;
import org.bigseven.result.AjaxResult;
import org.bigseven.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

//    @PostMapping("/register")
//    public BaseResponse<Long> register(@RequestBody @Valid UserRegisterRequest request) {
//        long userId = userService.register(request);
//        return BaseResponse.success(userId);
//    }

    @PostMapping("/login")
    public AjaxResult<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
        Integer id = userService.login(request.getUsername(), request.getPassword(), request.getEmail());
        return AjaxResult.success(new UserLoginResponse(id));
    }

}