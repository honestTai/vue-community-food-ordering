package com.takeOut.food.api.bean.dto;

import com.takeOut.food.api.entity.User;
import lombok.Data;

/**
 * 用户登录返回实体
 */
@Data
public class LoginDto {

    /**
     * 用户对象
     */
    private User user;

    /**
     * token
     */
    private String token;


    public LoginDto(User user, String token) {
        this.user = user;
        this.token = token;
    }
}
