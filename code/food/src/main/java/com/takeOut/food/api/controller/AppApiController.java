package com.takeOut.food.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.CommentDto;
import com.takeOut.food.api.bean.dto.OrderDto;
import com.takeOut.food.api.bean.general.result.Result;
import com.takeOut.food.api.bean.vto.*;
import com.takeOut.food.api.entity.*;
import com.takeOut.food.util.File;
import com.takeOut.food.util.UserLocal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.util.IdUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * App控制接口 需要登录后访问
 * </p>
 *
 * @author @author

 */
@RestController
@RequestMapping("/app")
public class AppApiController extends BaseController{

    /**
     * 获取用户的登录信息
     */
    @GetMapping("/appUser")
    public User user() {
        return UserLocal.getUser();
    }

    /**
     * 获取当前用户的订单数量
     * 且进行分组获取与返回
     * 0待付款
     * 1待收货/待消费
     * 2待确认
     * 3已完成
     */
    @GetMapping("/orderCount")
    public List<Integer> orderCount() {
        return orderService.orderCount(UserLocal.getUser().getId());
    }

    /**
     * 信息同步
     */
    @PostMapping("/appUserUpdate")
    public User appUserUpdate(@RequestBody AppUserVto appUserVto) {
        return userService.appUserUpdate(appUserVto);
    }

    /**
     * 收货地址获取
     */
    @PostMapping("/userAddress")
    public Result userAddress(@RequestBody AppPageVto appPageVto) {
        return Result.success(addressService.page(new Page<Address>(appPageVto.getCurrent(), appPageVto.getSize()), new QueryWrapper<Address>().eq("user_id", UserLocal.getUser().getId())));
    }

    /**
     * 添加收货地址
     */
    @PostMapping("/addAddress")
    public void addAddress(@RequestBody Address address) {
        if (address.getId() != null) {
            addressService.updateById(address);
        } else {
            address.setUserId(UserLocal.getUser().getId());
            addressService.save(address);
        }
    }

    /**
     * 收货地址删除
     */
    @DeleteMapping("/deleteAddress/{addressId}")
    public void deleteAddress(@PathVariable("addressId") Integer addressId) {
        addressService.removeById(addressId);
    }


    /**
     * 提交订单
     * 默认直接支付，没有相应证件，直接支付成功
     */
    @PostMapping("/orderAdd")
    @Transactional(rollbackFor = Exception.class)
    public void orderAdd(@RequestBody List<Order> orders) {
        orders.forEach(order -> {
            order.setOrderString(IdUtil.getSnowflake(0, 0).nextIdStr());
            order.setTime(new Date());
            order.setUserId(UserLocal.getUser().getId());
            order.setStatus(0);
            /**
             * 如果消费方式是0配送，订单状态为待收货
             * 如果消费方式是1到店，订单状态为待消费
             */
            order.setStatus(order.getType() == 0 ? 0 : 1);
            /**
             * 减少餐品库存
             * 判断买了多少，有多少减少多少
             */
            Goods goods = goodsService.getById(order.getGoodsId());
            goodsService.updateStock(order.getPaymentPrice().intValue() / goods.getPrice(), order.getGoodsId());
            order.setNum(order.getPaymentPrice().intValue() / goods.getPrice());
        });
        orderService.saveBatch(orders);
    }

    /**
     * 订单列表查看
     * status
     * current
     * size
     */
    @PostMapping("/orderPage")
    public Result orderPage(@RequestBody OrderPageVto orderPageVto) {
        return orderService.orderPage(orderPageVto);
    }

    /**
     * 订单取消
     */
    @PutMapping("/orderCancel/{id}")
    @Transactional(rollbackFor = Exception.class)
    public void orderCancel(@PathVariable Integer id) {
        Order order = orderService.getById(id);
        //库存恢复
        goodsService.updateStock(-order.getNum(), order.getGoodsId());
        orderService.updateStatus(id, 4);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/orderInfo/{id}")
    public OrderDto orderInfo(@PathVariable Integer id) {
        return orderService.orderInfo(id);
    }

    /**
     * 订单删除
     */
    @DeleteMapping("/orderDel/{id}")
    public void orderDel(@PathVariable Integer id) {
        orderService.removeById(id);
    }

    /**
     * 订单确认收货
     */
    @PutMapping("/orderReceive/{id}")
    public void orderReceive(@PathVariable Integer id) {
        orderService.orderReceive(id);
    }

    /**
     * 订单评价
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/orderCv")
    public void orderCv(@RequestBody Evaluation evaluation) {
        evaluation.setTime(new Date());
        evaluation.setUserId(UserLocal.getUser().getId());
        evaluationService.save(evaluation);
        //修改订单状态
        orderService.updateStatus(evaluation.getOrderId(), 3);
    }

    /**
     * 评价获取
     */
    @PostMapping("/userCommentPage")
    public Page<CommentDto> userCommentPage(@RequestBody AppPageVto appPageVto) {
        return evaluationService.userCommentPage(appPageVto);
    }




}
