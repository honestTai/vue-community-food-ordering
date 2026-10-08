package com.takeOut.food.api.bean.vto;

import com.takeOut.food.api.entity.*;
import com.takeOut.food.api.entity.Class;
import com.takeOut.food.api.entity.*;
import lombok.Data;

/**
 * 统一操作实体类
 * 包含订单，餐品，分类，用户
 * 操作类别，模块类别
 * 删除走另外接口
 */
@Data
public class OperationVto {

    //用户
    private User user;

    //订单
    private Order order;

    //餐品
    private Goods goods;

    //分类
    private Class aClass;

    //评论
    private Evaluation evaluation;

    //操作类型，operationType->0修改1新增2删除
    private Integer operationType;

    //所属模块, * 1用户模块
    //     * 2分类模块
    //     * 3餐品模块
    //     * 4订单模块（仅仅修改其状态），默认配送完成后台修改
    private Integer type;

    private TakeUser takeUser;

}
