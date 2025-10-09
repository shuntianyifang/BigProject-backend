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
 * 超级管理员用户管理控制器
 *
 * @author shuntianyifang
 * &#064;date 2025/10/4
 */
@RestController
@RequestMapping("/api/manage/user")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN')")
public class SuperAdminUserController {

    private final UserService userService;

    /**
     * 获取所有用户列表
     *
     * @param request 包含分页信息的用户请求参数
     * @return 返回包含用户列表和分页信息的结果对象
     */
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

    /**
     * 修改用户类型
     *
     * @param request 包含新用户类型的请求对象
     * @param id 需要修改类型的用户ID
     * @return 操作结果，成功时返回空内容的成功响应
     */
    @PostMapping("/{id}")
    public AjaxResult<Void> changeUserType(@RequestBody ChangeUserTypeRequest request,
                                           @PathVariable Integer id) {
        userService.changeUserType(id, request.getUserType());
        return AjaxResult.success();
    }

    /**
     * 删除用户
     *
     * @param id 需要删除的用户ID
     * @return 操作结果，成功时返回空内容的成功响应
     */
    @DeleteMapping("/{id}/delete")
    public AjaxResult<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return AjaxResult.success();
    }
}
