package org.bigseven.controller;

import jakarta.validation.Valid;
import org.bigseven.dto.rating.PublishRatingRequest;
import org.bigseven.dto.rating.UpdateRatingRequest;
import org.bigseven.dto.user.UpdateUserDetailRequest;
import org.bigseven.result.AjaxResult;
import org.bigseven.security.CustomUserDetails;
import org.bigseven.service.RatingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
@PreAuthorize("hasAuthority('ROLE_STUDENT')")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/publish")
    public AjaxResult<Void> publishRating(@Valid @RequestBody PublishRatingRequest request,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 从当前登录用户中获取user_id
        Integer userId = userDetails.getUserId();
        ratingService.publishRating(userId,
                request.getAdminReplyId(),
                request.getContent(),
                request.getScore()
        );
        return AjaxResult.success();
    }

    @PostMapping("/{id}")
    public AjaxResult<Void> updateRating(@Valid @RequestBody UpdateRatingRequest request,
                                             @AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Integer id) {
        String content = request.getContent();
        Integer score = request.getScore();
        ratingService.updateRating(id, userDetails, content, score);
        return AjaxResult.success();
    }
}
