package org.bigseven.controller;

import lombok.RequiredArgsConstructor;
import org.bigseven.constant.JwtConstants;
import org.bigseven.dto.feedback.GetFeedbackDetailResponse;
import org.bigseven.dto.user.GetUserDetailResponse;
import org.bigseven.result.AjaxResult;
import org.bigseven.service.UserService;
import org.bigseven.util.XssProtectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/3
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final XssProtectionUtils xssProtectionUtils;

    @GetMapping("{id}/profile")
    public AjaxResult<GetUserDetailResponse> getUserDetail(@PathVariable String id) {
        GetUserDetailResponse response = userService.getUserDetail(Integer.valueOf(id));
        return AjaxResult.success(response);
    }
}
