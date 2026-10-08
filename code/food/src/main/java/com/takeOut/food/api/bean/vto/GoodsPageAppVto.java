package com.takeOut.food.api.bean.vto;

import lombok.Data;

@Data
public class GoodsPageAppVto {
    private Integer current;

    private Integer size;

    private String name;

    private Integer categorySecond;

}
