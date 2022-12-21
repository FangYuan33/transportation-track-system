package com.tts.framework.common.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页对象
 *
 * @author ruoyi
 */
@Data
public class Pager implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    @TableField(exist = false)
    protected Integer pageNum;

    /**
     * 每页数量
     */
    @TableField(exist = false)
    protected Integer pageSize;

    /**
     * 排序字段英文名
     */
    @TableField(exist = false)
    protected String orderByColumn;

    /**
     * 是否正序
     * asc：正序，desc：倒序
     */
    @TableField(exist = false)
    protected String isAsc;
}
