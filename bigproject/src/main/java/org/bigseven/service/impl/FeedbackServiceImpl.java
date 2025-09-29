package org.bigseven.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigseven.config.FeedbackConfig;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.constant.FeedbackStatusEnum;
import org.bigseven.constant.FeedbackTypeEnum;
import org.bigseven.dto.admin.AdminFeedbackRequest;
import org.bigseven.dto.admin.AdminFeedbackResponse;
import org.bigseven.dto.user.UserSimpleVO;
import org.bigseven.entity.Feedback;
import org.bigseven.entity.FeedbackImage;
import org.bigseven.entity.User;
import org.bigseven.exception.ApiException;
import org.bigseven.mapper.FeedbackImageMapper;
import org.bigseven.mapper.FeedbackMapper;
import org.bigseven.mapper.UserMapper;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.service.FeedbackService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author v185v
 * &#064;date  2025/9/17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackMapper feedbackMapper;
    private final UserMapper userMapper;
    private final FeedbackImageMapper feedbackImageMapper;
    private final FeedbackConfig feedbackConfig;
    private static final String ASC_ORDER = "asc";

    /**
     * 发布用户反馈信息
     *
     * @param userId 用户ID
     * @param isNicked 是否匿名发布
     * @param isArgent 是否紧急反馈
     * @param feedbackType 反馈类型枚举
     * @param title 反馈标题
     * @param content 反馈内容
     * @param imageUrls 反馈图片URL列表，最多保存前9张图片
     */
    @Override
    public void publishFeedback(Integer userId, Boolean isNicked, Boolean isArgent, FeedbackTypeEnum feedbackType, String title, String content, List<String> imageUrls) {
        Feedback feedback = Feedback.builder()
                .userId(userId)
                .isNicked(isNicked)
                .isUrgent(isArgent)
                .feedbackType(feedbackType)
                .title(title)
                .content(content)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        feedbackMapper.insert(feedback);

        /// 保存图片信息
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (int i = 0; i < Math.min(imageUrls.size(), feedbackConfig.getMaxImages()); i++) {
                FeedbackImage image = FeedbackImage.builder()
                        .feedbackId(feedback.getFeedbackId())
                        .imageUrl(imageUrls.get(i))
                        .imageOrder(i)
                        .build();
                feedbackImageMapper.insert(image);
            }
        }
    }

    /**
     * 管理员标记反馈状态
     *
     * @param feedbackId     反馈ID
     * @param feedbackStatus 反馈状态枚举值
     */
    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public Integer markFeedback(Integer feedbackId, FeedbackStatusEnum feedbackStatus) {
        // 从Spring Security上下文中获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer currentUserId = userDetails.getUserId();

        // 查询反馈信息
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback == null) {
            throw new ApiException(ExceptionEnum.FEEDBACK_NOT_FOUND);
        }

        // 更新反馈状态
        feedback.setFeedbackStatus(feedbackStatus);
        feedback.setAcceptedByUserId(currentUserId);

        int updateCount = feedbackMapper.updateById(feedback);
        if (updateCount == 0) {
            throw new ApiException(ExceptionEnum.OPERATION_FAILED);
        }

        return currentUserId;
    }

    /**
     * 删除反馈信息
     * @param userId 用户ID
     * @param feedbackId 反馈ID
     */
    @Override
    public void deleteFeedback(Integer userId,Integer feedbackId) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback != null) {
            /// 验证用户权限，不能删除其他用户的反馈
            if (feedback.getUserId().equals(userId)) {
                throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
            }
            /// 执行删除操作
            feedbackMapper.deleteById(feedbackId);
        } else {
            throw new ApiException(ExceptionEnum.RESOURCE_NOT_FOUND);
        }
    }

    /**
     * 普通User更新反馈信息
     * @param feedbackId 反馈ID
     * @param feedbackType 反馈类型
     * @param title 反馈标题
     * @param content 反馈内容
     */
    @Override
    public void updateFeedback(Integer userId,Integer feedbackId, FeedbackTypeEnum feedbackType, String title, String content) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback != null) {
            if(feedback.getUserId().equals(userId)){
                feedback.setFeedbackType(feedbackType);
                feedback.setTitle(title);
                feedback.setContent(content);
                feedbackMapper.updateById(feedback);
            }
            else {
                throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
            }
        }
        else {
            //需要做错误处理
            return;
        }

    }

//    @Override
//    public void getAllFeedbackByType(FeedbackTypeEnum feedbackType){
//
//        LambdaQueryWrapper<Feedback> feedbackQueryWrapper = new LambdaQueryWrapper<>();
//        feedbackQueryWrapper.eq(Feedback::getFeedbackType, feedbackType);
//        feedbackQueryWrapper.orderByDesc(Feedback::getFeedbackId);
//        feedbackMapper.selectList(feedbackQueryWrapper);
//
//        List<Feedback> feedbackListByType = feedbackMapper.selectList(feedbackQueryWrapper);
//
//    }

    /**
     * 根据条件查询所有反馈信息并分页返回
     * @param request 包含查询条件和分页参数的请求对象
     * @return 分页的反馈响应对象列表
     */
    @Override
    public Page<AdminFeedbackResponse> getAllFeedbacks(AdminFeedbackRequest request) {
        // 创建分页对象
        Page<Feedback> page = new Page<>(request.getPage(), request.getSize());

        // 构建查询条件
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();

        // 条件筛选
        applyLikeCondition(queryWrapper, "title", request.getTitle());
        applyEqualCondition(queryWrapper, "feedback_type", request.getFeedbackType());
        applyEqualCondition(queryWrapper, "feedback_status", request.getFeedbackStatus());
        applyEqualCondition(queryWrapper, "is_urgent", request.getIsUrgent());
        applyEqualCondition(queryWrapper, "student_id", request.getStudentId());
        applyEqualCondition(queryWrapper, "admin_id", request.getAdminId());
        applyDateCondition(queryWrapper, "created_at", request.getStartTime(), request.getEndTime());

        // 排序
        if (ASC_ORDER.equalsIgnoreCase(request.getSortOrder())) {
            queryWrapper.orderByAsc(request.getSortField());
        } else {
            queryWrapper.orderByDesc(request.getSortField());
        }

        // 执行查询
        IPage<Feedback> feedbackPage = feedbackMapper.selectPage(page, queryWrapper);

        // 转换为响应对象
        Page<AdminFeedbackResponse> responsePage = new Page<>();
        BeanUtils.copyProperties(feedbackPage, responsePage);

        List<AdminFeedbackResponse> feedbackResponses = feedbackPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        responsePage.setRecords(feedbackResponses);
        return responsePage;
    }


    /**
     * 根据ID获取反馈详情
     * @param id 反馈ID
     * @return 反馈详情响应对象
     * @throws ApiException 当反馈不存在时抛出异常
     */
    @Override
    public AdminFeedbackResponse getFeedbackDetail(Integer id) {
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback == null) {
            throw new ApiException(ExceptionEnum.NOT_FOUND_ERROR);
        }
        return convertToResponse(feedback);
    }


    private void applyLikeCondition(QueryWrapper<Feedback> wrapper, String field, String value) {
        if (StringUtils.hasText(value)) {
            wrapper.like(field, value);
        }
    }

    private void applyEqualCondition(QueryWrapper<Feedback> wrapper, String field, Object value) {
        if (value != null) {
            wrapper.eq(field, value);
        }
    }

    private void applyDateCondition(QueryWrapper<Feedback> wrapper, String field, String start, String end) {
        if (StringUtils.hasText(start)) {
            wrapper.ge(field, start);
        }
        if (StringUtils.hasText(end)) {
            wrapper.le(field, end);
        }
    }

    /**
     * 将Feedback对象转换为AdminFeedbackResponse对象
     * @param feedback Feedback实体对象
     * @return 转换后的AdminFeedbackResponse对象
     */
    private AdminFeedbackResponse convertToResponse(Feedback feedback) {
        AdminFeedbackResponse response = new AdminFeedbackResponse();
        BeanUtils.copyProperties(feedback, response);

        // 设置学生信息
        User student = userMapper.selectById(feedback.getUserId());
        if (student != null) {
            UserSimpleVO studentVO = new UserSimpleVO();
            BeanUtils.copyProperties(student, studentVO);
            response.setStudent(studentVO);
        }

        // 设置管理员信息（如果已分配）
        if (feedback.getUserId() != null) {
            User admin = userMapper.selectById(feedback.getUserId());
            if (admin != null) {
                UserSimpleVO adminVO = new UserSimpleVO();
                BeanUtils.copyProperties(admin, adminVO);
                response.setAdmin(adminVO);
            }
        }

        // 设置图片URL列表
        List<String> imageUrls = getFeedbackImageUrls(feedback.getFeedbackId());
        response.setImageUrls(imageUrls);

        return response;
    }

    /**
     * 获取反馈的图片URL列表
     * @param feedbackId 反馈ID
     * @return 图片URL列表
     */
    private List<String> getFeedbackImageUrls(Integer feedbackId) {
        QueryWrapper<FeedbackImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("feedback_id", feedbackId);
        List<FeedbackImage> images = feedbackImageMapper.selectList(queryWrapper);

        return images.stream()
                .map(FeedbackImage::getImageUrl)
                .map(this::buildImageUrl)
                .collect(Collectors.toList());
    }

    /**
     * 构建完整的图片URL
     * @param imagePath 图片路径
     * @return 完整的图片URL
     */
    private String buildImageUrl(String imagePath) {
        // 这里需要根据实际的文件访问路径来构建URL
        // 例如：http://your-domain.com/api/file/images/filename.jpg
        return "/api/file/images/" + imagePath;
    }
}
