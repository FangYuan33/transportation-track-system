package com.tts.framework.common.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author ruoyi
 */
public abstract class SimpleBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    @TableField(exist = false)
    @JsonIgnore
    protected String searchValue;

    /**
     * 分页对象
     */
    @TableField(exist = false)
    protected Pager pager;

    /**
     * 请求参数
     */
    @TableField(exist = false)
    protected Map<String, Object> params;

    /**
     * 操作符
     */
    @TableField(exist = false)
    protected String queryOps;

    /**
     * 扩展查询条件
     */
    @TableField(exist = false)
    protected String advancedQueryCondition;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}
