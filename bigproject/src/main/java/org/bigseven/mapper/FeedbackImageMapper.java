package org.bigseven.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.bigseven.entity.FeedbackImage;

import java.util.List;

/**
 * 反馈图片Mapper
 *
 * @author v185v
 * &#064;date 2025/9/18
 */
public interface FeedbackImageMapper extends BaseMapper<FeedbackImage> {
    /**
     * @param feedbackId 反馈唯一标识ID
     * @return 对应的反馈实体对象，如果不存在则返回null
     */
    @Select("SELECT image_url FROM feedback_image WHERE feedback_id = #{feedback_id}")
    List<String> selectImageUrlsByFeedbackId(@Param("feedback_id") Integer feedbackId);
}
