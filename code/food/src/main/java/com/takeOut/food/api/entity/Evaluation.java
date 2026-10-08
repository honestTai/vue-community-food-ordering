package com.takeOut.food.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评价实体
 * </p>
 *
 * @author @author

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 时间
     */
    private Date time;

    /**
     * 评价人
     */
    private Integer userId;

    /**
     * 回复id
     */
    private Integer replyId;

    private Integer goodsId;

    /**
     * 订单id
     */
    @TableField(exist = false)
    private Integer orderId;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date replyTime;

    /**
     * 是否回复
     * 0未1回复
     */
    private Integer replyStatus;


}
