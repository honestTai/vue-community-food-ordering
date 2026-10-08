package com.takeOut.food.api.bean.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.util.List;

/**
 * 餐品订单一对多导出
 */
@Data
@ExcelTarget("ExportDtoGoods")
public class ExportDtoGoods {

    private Integer goodsId;
    /**
     * 餐品名称
     */
    @Excel(name = "餐品名称", orderNum = "1", width = 25, needMerge = true)
    private String goodsName;

    @ExcelCollection(name = "顾客下单信息", orderNum = "4")
    private List<ExportDtoCustomer> exportDtoCustomers;
}
