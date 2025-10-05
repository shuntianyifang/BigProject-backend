package org.bigseven.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.bigseven.dto.base.BaseListResponse;
import org.bigseven.dto.superadmin.ChangeUserTypeRequest;
import org.bigseven.dto.user.GetAllUserRequest;
import org.bigseven.dto.user.GetAllUserResponse;
import org.bigseven.result.AjaxResult;
import org.bigseven.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author shuntianyifang
 * &#064;date 2025/10/4
 */
@RestController
@RequestMapping("/api/manage/user")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN')")
public class SuperAdminUserController {

    private final UserService userService;

    @GetMapping("/list")
    public AjaxResult<BaseListResponse<GetAllUserResponse>> getAllUsers(GetAllUserRequest request) {

        Page<GetAllUserResponse> pageResult = userService.getAllUsers(request);

        BaseListResponse<GetAllUserResponse> response = BaseListResponse.<GetAllUserResponse>builder()
                .list(pageResult.getRecords())
                .total((int) pageResult.getTotal())
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .totalPages((int) pageResult.getPages())
                .build();

        return AjaxResult.success(response);
    }

    @PostMapping("/{id}")
    public AjaxResult<Void> changeUserType(@RequestBody ChangeUserTypeRequest request,
                                           @PathVariable Integer id) {
        userService.changeUserType(id, request.getUserType());
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}/delete")
    public AjaxResult<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return AjaxResult.success();
    }
}
