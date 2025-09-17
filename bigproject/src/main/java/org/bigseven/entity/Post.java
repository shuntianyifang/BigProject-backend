package org.bigseven.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bigseven.constant.PostTypeEnum;

import java.time.LocalDateTime;

/**
 * Description:这是学生问题帖子实体类,与管理员反馈不是同一个实体类
 * @TableName post
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @TableId(type = IdType.AUTO)
    private Integer postId;

    private String title;

    private String content;

    private Integer userId;

    private Integer viewCount;

    //接单的管理员id
    private Integer acceptedByUserId;

    //帖子类型
    private PostTypeEnum postType;

    //是否匿名
    private Boolean isNicked;

    //是否紧急
    private Boolean isArgent;

    //是否已经由管理员接单
    private Boolean isAccepted;

    //是否已经反馈完成
    private Boolean isResolved;

    //是否为垃圾信息
    private Boolean isTrash;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
