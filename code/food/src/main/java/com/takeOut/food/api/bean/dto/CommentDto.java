package com.takeOut.food.api.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
/**
 * 需要评价内容，评价用户名称，评价id,评价时间
 */
public class CommentDto {

    private String content;

    private String userName;

    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

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

    private String headimgUrl;

    private String goodsName;

    private Integer goodsId;

}
