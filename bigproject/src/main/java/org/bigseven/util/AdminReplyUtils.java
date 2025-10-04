package org.bigseven.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.bigseven.dto.adminreply.AdminReplyVO;
import org.bigseven.entity.AdminReply;
import org.bigseven.entity.User;
import org.bigseven.mapper.AdminReplyMapper;
import org.bigseven.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员回复服务
 *
 * @author shuntianyifang
 * &#064;date 2025/10/4
 */
@Component
public class AdminReplyUtils {

    private final AdminReplyMapper adminReplyMapper;
    private final UserMapper userMapper;
    private final UserConverterUtils userConverterUtils;

    public AdminReplyUtils(AdminReplyMapper adminReplyMapper, UserMapper userMapper, UserConverterUtils userConverterUtils) {
        this.adminReplyMapper = adminReplyMapper;
        this.userMapper = userMapper;
        this.userConverterUtils = userConverterUtils;
    }

    /**
     * 根据反馈ID获取管理员回复列表
     */
    public List<AdminReplyVO> getRepliesByFeedbackId(Integer feedbackId) {
        List<AdminReply> replies = adminReplyMapper.selectList(
                new QueryWrapper<AdminReply>()
                        .eq("feedback_id", feedbackId)
                        .orderByAsc("created_at")
        );

        return replies.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 转换管理员回复实体为响应对象
     */
    private AdminReplyVO convertToResponse(AdminReply reply) {
        AdminReplyVO response = AdminReplyVO.builder()
                .adminReplyId(reply.getAdminReplyId())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();

        // 设置管理员信息
        if (reply.getUserId() != null) {
            User adminUser = userMapper.selectById(reply.getUserId());
            if (adminUser != null) {
                response.setAdmin(userConverterUtils.toUserSimpleVO(adminUser));
            }
        }

        return response;
    }
}