package org.bigseven.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.bigseven.entity.Feedback;

/**
 * @author v185v
 * &#064;date 2025/9/17
 */
public interface FeedbackMapper extends BaseMapper<Feedback> {
    @Update("UPDATE feedback SET view_count = view_count + 1 WHERE feedback_id = #{feedback_id}")
    void incrementViewCount(@Param("feedback_id") Integer feedbackId);
}
