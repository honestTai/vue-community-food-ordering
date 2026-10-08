package com.takeOut.food.api.bean.vto;

import lombok.Data;

import java.util.List;

@Data
public class AppPageVto {

    private Integer current;

    private Integer size;

    private List<Integer> ids;

    private Integer goodsId;
}
