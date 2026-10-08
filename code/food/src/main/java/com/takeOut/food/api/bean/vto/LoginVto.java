package com.takeOut.food.api.bean.vto;

import lombok.Data;

/**
 * 用户登录参数
 */
@Data
public class LoginVto {

    /**
     * 登录账号
     */
    private String number;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 登录来源
     * 0App
     * 1后台
     */
    private Integer from;

}
