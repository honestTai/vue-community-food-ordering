package com.takeOut.food.api.bean.vto;

import lombok.Data;

@Data
public class OrderPageVto {

    private Integer current;

    private Integer size;

    private Integer status;

    private Integer userId;

}
