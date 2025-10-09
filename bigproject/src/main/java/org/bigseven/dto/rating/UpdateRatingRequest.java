package org.bigseven.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 修改评价请求数据传输对象
 *
 * @author shuntianyifang
 * &#064;date 2025/10/5
 */
@Data
@AllArgsConstructor
@Builder
public class UpdateRatingRequest {
    /**
     * 评价内容
     */
    private String content;
    /**
     * 评价分数
     */
    private Integer score;
}
