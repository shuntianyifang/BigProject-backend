package org.bigseven.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigseven.constant.PostTypeEnum;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Builder
public class PublishPostRequest {

    private Integer postId;

    private String title;

    private String content;

    //帖子类型
    private PostTypeEnum postType;

    //是否匿名
    private Boolean isNicked;

    //是否紧急
    private Boolean isArgent;

    //发布帖子的用户的id
    private Integer userId;

    //是否已经由管理员接单
    private Boolean isAccepted;

    //接单的管理员id
    private Integer acceptedByUserId;

    //是否已经反馈完成
    private Boolean isResolved;

    //是否为垃圾信息
    private Boolean isTrash;

    private Integer viewCount;

    private Boolean deleted;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;
}
