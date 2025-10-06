package org.bigseven.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.bigseven.entity.FeedbackImage;
import org.bigseven.mapper.FeedbackImageMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 反馈图片工具类
 *
 * @author shuntianyifang
 * &#064;date 2025/10/3
 */
@Component
public class FeedbackImageUtils {

    private final FeedbackImageMapper feedbackImageMapper;

    @Value("${feedback.image-base-url:/api/file/uploads/}")
    private String imageBaseUrl;

    public FeedbackImageUtils(FeedbackImageMapper feedbackImageMapper) {
        this.feedbackImageMapper = feedbackImageMapper;
    }

    /**
     * 根据反馈ID获取相关的图片URL列表
     *
     * @param feedbackId 反馈的唯一标识ID
     * @return 包含完整URL的图片地址列表
     */
    public List<String> getFeedbackImageUrls(Integer feedbackId) {
        List<FeedbackImage> images = feedbackImageMapper.selectList(
                new QueryWrapper<FeedbackImage>().eq("feedback_id", feedbackId)
        );

        return images.stream()
                .map(FeedbackImage::getImageUrl)
                .map(this::buildImageUrl)
                .collect(Collectors.toList());
    }

    /**
     * 构建完整的图片URL
     */
    public String buildImageUrl(String imagePath) {
        return imageBaseUrl + imagePath;
    }

    /**
     * 保存反馈图片（存储相对路径）
     */
    public void saveFeedbackImages(Integer feedbackId, List<String> imageUrls, int maxImages) {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (int i = 0; i < Math.min(imageUrls.size(), maxImages); i++) {
                String imageUrl = imageUrls.get(i);
                // 移除可能已存在的基础URL前缀
                if (imageUrl.startsWith(imageBaseUrl)) {
                    imageUrl = imageUrl.substring(imageBaseUrl.length());
                }

                FeedbackImage image = FeedbackImage.builder()
                        .feedbackId(feedbackId)
                        .imageUrl(imageUrl)
                        .imageOrder(i)
                        .build();
                feedbackImageMapper.insert(image);
            }
        }
    }

    public void deleteImagesByFeedbackId(Integer id) {
        feedbackImageMapper.delete(new QueryWrapper<FeedbackImage>().eq("feedback_id", id));
    }
}