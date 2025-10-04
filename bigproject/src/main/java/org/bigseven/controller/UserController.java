package org.bigseven.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bigseven.dto.user.GetUserDetailResponse;
import org.bigseven.dto.user.UpdateUserDetailRequest;
import org.bigseven.result.AjaxResult;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.service.UserService;
import org.bigseven.util.XssProtectionUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("{id}/profile")
    public AjaxResult<Void> updateUserDetail(@Valid @RequestBody UpdateUserDetailRequest request,
                                             @AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable String id) {

        String email = xssProtectionUtils.escapeHtml(xssProtectionUtils.sanitize(request.getEmail()));
        String userPhone = xssProtectionUtils.escapeHtml(xssProtectionUtils.sanitize(request.getUserPhone()));
        String nickname = xssProtectionUtils.escapeHtml(xssProtectionUtils.sanitize(request.getNickname()));
        String realName = xssProtectionUtils.escapeHtml(xssProtectionUtils.sanitize(request.getRealName()));

        userService.updateUserDetail(Integer.valueOf(id), userDetails, email, userPhone, nickname, realName);
        return AjaxResult.success();
    }
}
