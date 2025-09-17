package org.bigseven.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "admin_reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminReply {

    @TableId(type = IdType.AUTO)
    @TableField("admin_reply_id")
    private Integer AdminReplyId;

    //反馈的目标帖子id
    @TableField("target_feedback_id")
    private Integer targetFeedbackId;

    private String title;

    private String content;

    //用userId判断userType来鉴权,非管理员不能发送反馈
    @TableField("user_id")
    private Integer userId;
}
