package com.takeOut.food.api.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单实体
 * </p>
 *
 * @author @author

 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 餐品主键
     */
    private Integer goodsId;

    /**
     * 地址主键
     */
    private Integer addressId;

    /**
     * 下单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    /**
     * 0配送1到店
     */
    private Integer type;

    /**
     * 0待结账1待消费/待收货2待评价3已取消4订单完成
     */
    private Integer status;

    /**
     * 送达时间/消费时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date getTime;

    @TableField(value = "user_id")
    private Integer userId;

    @TableField(value = "order_string")
    private String orderString;

    @TableField(value = "payment_price")
    private BigDecimal paymentPrice;

    @TableField(exist = false)
    private Integer userCatsId;

    /**
     * 数量
     */
    private Integer num;


}
