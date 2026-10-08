package com.takeOut.food.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author @author
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户表姓名
     */
    private String name;

    /**
     * 登录账号
     * 手机登录
     * 电话号码
     * 前台用户
     */
    private String phone;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") //Jackson包使用注解
    private Date time;

    /**
     * 0顾客2管理员3禁止登录
     */
    private Integer type;


    /**
     * 登录账号
     * 后台用户
     */
    private String number;

    /**
     * 登录密码
     * app用户与后台用户
     */
    private String password;

    private String headimgUrl;

    @TableField(exist = false)
    private Integer cartId = 0;


}
