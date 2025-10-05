package org.bigseven.service;

import org.bigseven.security.CustomUserDetails;

public interface RatingService {
    void publishRating(Integer userId, Integer adminReplyId, String content, Integer score);

    void updateRating(Integer id, CustomUserDetails userDetails, String content, Integer score);

    void deleteRating(Integer id, CustomUserDetails userDetails);
}
