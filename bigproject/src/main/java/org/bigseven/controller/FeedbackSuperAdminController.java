package org.bigseven.controller;

import org.bigseven.service.FeedbackService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * （没用到）
 *
 * @author v185v
 * &#064;date 2025/9/25
 */
@RestController
@RequestMapping("/api/superadmin/feedback")
@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
public class FeedbackSuperAdminController {
    private final FeedbackService feedbackService;
    public FeedbackSuperAdminController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }



}
