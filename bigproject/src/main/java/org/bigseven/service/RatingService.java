package org.bigseven.service;

import org.bigseven.security.CustomUserDetails;

/**
 * 用户评分服务接口
 *
 * @author shuntianyifang
 */
public interface RatingService {

    /**
     * 发布新的用户评分
     *
     * @param userId 用户ID
     * @param adminReplyId 管理员回复ID
     * @param feedbackId 反馈ID
     * @param content 评分内容
     * @param score 评分分数
     */
    void publishRating(Integer userId, Integer adminReplyId, Integer feedbackId, String content, Integer score);

    /**
     * 修改用户评分
     *
     * @param id 评分ID
     * @param userDetails 修改评分的用户
     * @param content 评分内容
     * @param score 评分分数
     */
    void updateRating(Integer id, CustomUserDetails userDetails, String content, Integer score);

    /**
     * 删除用户评分
     *
     * @param id 评分ID
     * @param userDetails 删除评分的用户
     */
    void deleteRating(Integer id, CustomUserDetails userDetails);
}
