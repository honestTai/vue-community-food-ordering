package com.takeOut.food.api.bean.vto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ListVto {

    /**
     * 名称模糊查询
     */
    private String likeName;

    /**
     * 餐品分类标识
     */
    private Integer classId;

    /**
     * 时间查询
     */
    private List<Date> dateList;

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 列表处理Type
     * 0顾客信息列表
     * 1后台用户信息列表
     * 2分类列表
     * 3餐品列表
     * 4订单列表
     */
    private Integer listType;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 用户类型
     */
    private Integer userType;

    private String orderString;

    private Integer goodsId;

    private String dateListString;
}
