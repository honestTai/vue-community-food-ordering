package com.takeOut.food.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 餐品实体
 * </p>
 *
 * @author @author

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 介绍，富文本
     */
    private String introduce;

    /**
     * 4张图片
     */
    private String images;

    /**
     * 分类Id
     */
    private Integer classId;


    @TableField(exist = false)
    private List<String> goodsPhotoList;

    //订单数量
    @TableField(exist = false)
    private Integer goodsOrderCount;


    private Integer status;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 简介
     */
    private String sort_introduce;


}
