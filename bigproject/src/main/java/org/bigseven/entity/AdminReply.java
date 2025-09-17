package org.bigseven.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminReply {

    @TableId(type = IdType.AUTO)
    private Integer AdminReplyId;

    //反馈的目标帖子id
    private Integer targetFeedbackId;

    private String title;

    private String content;

    //用userId判断userType来鉴权,非管理员不能发送反馈
    private Integer userId;
}
