package org.bigseven.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UpdateRatingRequest {
    private String content;
    private Integer score;
}
