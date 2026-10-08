package com.takeOut.food.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 骑手
 */
@Data
@TableName(value = "takeuser")
public class TakeUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 详情主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "order_id")
    private Integer orderId;

    @TableField(value = "name")
    private String name;

    @TableField(value = "phone")
    private String phone;
}
