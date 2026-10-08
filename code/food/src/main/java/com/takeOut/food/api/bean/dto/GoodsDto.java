package com.takeOut.food.api.bean.dto;

import com.takeOut.food.api.entity.Class;
import com.takeOut.food.api.entity.Goods;
import lombok.Data;

@Data
public class GoodsDto {

    //分类
    private Class aClass;

    //餐品
    private Goods goods;

    //评价数
    private Integer commentTotal;
}
