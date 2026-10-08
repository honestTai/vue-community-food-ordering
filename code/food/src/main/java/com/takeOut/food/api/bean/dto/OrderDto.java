package com.takeOut.food.api.bean.dto;

import com.takeOut.food.api.entity.*;
import lombok.Data;

/**
 * 订单DTO
 */

@Data
public class OrderDto {

    private User user;

    private Order order;

    private Address address;

    private Goods goods;

    private TakeUser takeUser;

}
