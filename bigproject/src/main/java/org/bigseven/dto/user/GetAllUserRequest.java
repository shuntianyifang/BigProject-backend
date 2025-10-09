package org.bigseven.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bigseven.constant.UserTypeEnum;

import java.util.List;

/**
 * 管理员反馈请求数据传输对象
 * 用于封装管理员对反馈进行查询、处理等操作时的请求参数
 *
 * @author v185v
 * &#064;date 2025/9/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllUserRequest {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 分页页码，默认为1
     */
    private Integer page;

    /**
     * 分页大小，默认为10
     */
    private Integer size;

    /**
     * 用户名关键字，用于按标题模糊查询
     */
    private String usernameKeyword;

    /**
     * 昵称关键字，用于按内容模糊查询
     */
    private String nicknameKeyword;

    /**
     *  真实姓名关键字
     */
    private String realNameKeyword;

    /**
     * 用户类型
     */
    private List<String> typeTags;

    /**
     * 用户状态
     */
    private boolean deleted;

    /**
     * 查询时间范围的起始时间
     */
    private String fromTime;

    /**
     * 查询时间范围的结束时间
     */
    private String toTime;

    /**
     * 排序字段，默认为"createdAt"
     */
    private String sortField;

    /**
     * 排序顺序，默认为"desc"(降序)
     */
    private String sortOrder;
}