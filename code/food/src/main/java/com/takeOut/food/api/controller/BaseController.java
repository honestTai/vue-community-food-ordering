package com.takeOut.food.api.controller;

import com.takeOut.food.api.bean.general.result.ResultException;
import com.takeOut.food.api.bean.general.result.ResultStatus;
import com.takeOut.food.api.service.*;
import com.takeOut.food.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * 需要注入Bean，但不能在对象类使用的方法，写在这个对象中
 */
public class BaseController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    ClassService classService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    EvaluationService evaluationService;

    @Autowired
    AddressService addressService;

    @Value("${file-path}")
    public String filePath;

    @Value("${file-url:http://127.0.0.1:9700/images/}")
    public String fileUrl;
}
