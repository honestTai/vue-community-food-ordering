package com.takeOut.food.api.bean.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.util.Date;


@Data
@ExcelTarget("ExportDtoCustomer")
public class ExportDtoCustomer {

    @Excel(name = "姓名", orderNum = "2", width = 25)
    private String customerName;

    @Excel(name = "电话", orderNum = "2", width = 25)
    private String customerPhone;

    @Excel(name = "收货地址", orderNum = "2", width = 25)
    private String customerAddress;

    @Excel(name = "下单时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd",orderNum = "3")
    private Date time;

    @Excel(name = "购买价格", orderNum = "1", width = 25)
    private String orderPrice;

    @Excel(name = "订单状态", width = 25)
    private String orderStatus;
}
