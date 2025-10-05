package org.bigseven.service.impl;

import lombok.RequiredArgsConstructor;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.entity.Rating;
import org.bigseven.exception.ApiException;
import org.bigseven.mapper.RatingMapper;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.service.RatingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingMapper ratingMapper;

    @Override
    public void publishRating(Integer userId, Integer adminReplyId, String content, Integer score) {

        Rating existingRating = ratingMapper.selectById(adminReplyId);

        if (existingRating != null) {
            throw new ApiException(ExceptionEnum.RATING_EXIST);
        }

        Rating rating = Rating.builder()
                .userId(userId)
                .adminReplyId(adminReplyId)
                .content(content)
                .score(score)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        ratingMapper.insert(rating);
    }

    @Override
    public void updateRating(Integer id, CustomUserDetails userDetails, String content, Integer score) {
        Rating existingRating = ratingMapper.selectById(id);
        if (existingRating == null || existingRating.getDeleted()) {
            throw new ApiException(ExceptionEnum.RATING_NOT_FOUND);
        }
        if (!existingRating.getUserId().equals(userDetails.getUserId())) {
            throw new ApiException(ExceptionEnum.PERMISSION_DENIED);
        }
        existingRating.setContent(content);
        existingRating.setScore(score);
        existingRating.setUpdatedAt(LocalDateTime.now());
        ratingMapper.updateById(existingRating);
    }
}
