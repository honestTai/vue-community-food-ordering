package com.takeOut.food.api.bean.dto;

import lombok.Data;

@Data
public class GoodsSpecDto {

    /**
     * 规格
     * 0大
     * 1中
     * 2小
     */
    private String specType;

    /**
     * 价钱
     */
    private String price;

    /**
     * 库存
     */
    private String stock;
}
