package org.bigseven.util;

import org.bigseven.dto.feedback.GetAllFeedbackResponse;
import org.bigseven.dto.feedback.GetFeedbackDetailResponse;
import org.bigseven.dto.user.GetAllUserResponse;
import org.bigseven.entity.Feedback;
import org.bigseven.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 反馈响应构建器
 *
 * @author shuntianyifang
 * &#064;date 2025/10/3
 */
@Component
public class UserResponseBuilder {

    private final UserAuthenticationQueryUtils userAuthenticationQueryUtils;

    /**
     * 内容标记常量
     */
    public static final String NULL_CONTENT_MARKER = "nulla";

    public UserResponseBuilder(UserAuthenticationQueryUtils userAuthenticationQueryUtils) {
        this.userAuthenticationQueryUtils = userAuthenticationQueryUtils;
    }

    /**
     * 构建基础反馈响应
     */
    public void buildBaseUserResponse(Object response, User user) {
        if (user == null) {
            return;
        }

        // 设置头像URL（没做完）
        //List<String> imageUrls = feedbackImageUtils.getFeedbackImageUrls(feedback.getFeedbackId());
        //setImageUrlsToResponse(response, imageUrls);
    }

    /**
     * 转换到GetAllFeedbackResponse
     */
    public GetAllUserResponse convertToGetAllResponse(User user) {
        if (user == null) {
            return null;
        }

        GetAllUserResponse response = new GetAllUserResponse();
        BeanUtils.copyProperties(user, response);

        buildBaseUserResponse(response, user);
        return response;
    }

}