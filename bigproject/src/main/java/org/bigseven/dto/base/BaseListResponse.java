package org.bigseven.dto.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 列表返回对象
 *
 * @author shuntianyifang
 * &#064;date 2025/9/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseListResponse<T> {
    private List<T> list;
    /**
     * 总记录数
     */
    private Integer total;
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 每页大小
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPages;
}