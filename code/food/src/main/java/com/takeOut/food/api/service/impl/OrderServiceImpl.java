package com.takeOut.food.api.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.DownLoadDto;
import com.takeOut.food.api.bean.dto.ExportDtoGoods;
import com.takeOut.food.api.bean.dto.OrderDto;
import com.takeOut.food.api.bean.dto.StatisticsOrderDto;
import com.takeOut.food.api.bean.general.result.Result;
import com.takeOut.food.api.bean.general.result.ResultException;
import com.takeOut.food.api.bean.general.result.ResultStatus;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.bean.vto.OperationVto;
import com.takeOut.food.api.bean.vto.OrderPageVto;
import com.takeOut.food.api.bean.vto.StatisticsVto;
import com.takeOut.food.api.entity.Order;
import com.takeOut.food.api.entity.TakeUser;
import com.takeOut.food.api.mapper.OrderMapper;
import com.takeOut.food.api.mapper.TakeUserMapper;
import com.takeOut.food.api.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.takeOut.food.util.File;
import com.takeOut.food.util.RedisUtil;
import com.takeOut.food.util.UserLocal;
import com.takeOut.food.util.encryption.MD5Util;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static com.takeOut.food.constant.operationConstants.DELETE;
import static com.takeOut.food.constant.operationConstants.UPDATE;
import static com.takeOut.food.util.BaseFunction.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @author
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    TakeUserMapper takeUserMapper;

    /**
     * 用户手机号精确，用户姓名模糊，分类id,餐品名称
     * 多表关联查询
     * 用户表，收货地址表，餐品信息表，订单表
     * 返回新的DTO->List<>
     * 登录用户有公司，只查询该公司下的订单
     *
     * @param listVto
     * @return
     */
    @Override
    public Page<?> orderList(ListVto listVto) {
        Page<OrderDto> page = new Page<>(listVto.getPage(), listVto.getPageSize());
        IPage<OrderDto> orderDtoIPage = orderMapper.selectByPage(page, listVto);
        orderDtoIPage.getRecords().forEach(orderDto -> {
            orderDto.getGoods().setGoodsPhotoList(stringToList(orderDto.getGoods().getImages()));
        });
        return (Page<OrderDto>) orderDtoIPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationOrder(OperationVto operationVto, Integer operationType) throws ResultException {
        /**
         * 删除，发货，消费
         * 发货与消息是修改接口
         * 是不是订单所有者鉴权
         * 只能单次操作
         */
        Order order = operationVto.getOrder();
        if (operationType == UPDATE) {
            if (order.getStatus() == 0) {
                operationVto.getTakeUser().setOrderId(order.getId());
                takeUserMapper.insert(operationVto.getTakeUser());
                order.setStatus(5);
            } else {
                if (order.getStatus() == 1) {
                    order.setGetTime(new Date());
                }
                order.setStatus(2);
            }
            orderMapper.updateById(order);
        } else if (operationType == DELETE) {
            orderMapper.deleteById(order.getId());
        }


    }

    /**
     * 0待收货
     * 1待消费
     * 2待评价
     * 3已完成
     * 5待发货
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> orderCount(Integer id) {
        List<Integer> orders = new ArrayList<>();
        orders.add(orderMapper.selectCountByLoginUser(id, 0));
        orders.add(orderMapper.selectCountByLoginUser(id, 5));
        orders.add(orderMapper.selectCountByLoginUser(id, 1));
        orders.add(orderMapper.selectCountByLoginUser(id, 2));
        orders.add(orderMapper.selectCountByLoginUser(id, 3));
        return orders;
    }

    @Override
    public Result orderPage(OrderPageVto orderPageVto) {
        orderPageVto.setUserId(UserLocal.getUser().getId());
        IPage<OrderDto> orderDtoIPage = orderMapper.orderPage(new Page<OrderDto>(orderPageVto.getCurrent(), orderPageVto.getSize()), orderPageVto);
        orderDtoIPage.getRecords().forEach(orderDto -> {
            orderDto.getGoods().setGoodsPhotoList(stringToList(orderDto.getGoods().getImages()));
        });
        return Result.success((Page) orderDtoIPage);
    }

    @Override
    public void updateStatus(Integer id, int i) {
        orderMapper.updateStatus(id, i);
    }

    @Override
    public OrderDto orderInfo(Integer id) {
        OrderDto orderDto = orderMapper.orderInfo(id);
        if (orderDto.getOrder().getStatus() == 5) {
            //外卖小哥
            orderDto.setTakeUser(takeUserMapper.selectOne(new QueryWrapper<TakeUser>().eq("order_id", orderDto.getOrder().getId())));
        }
        orderDto.getGoods().setGoodsPhotoList(stringToList(orderDto.getGoods().getImages()));
        return orderDto;
    }

    @Override
    public void orderReceive(Integer id) {
        orderMapper.orderReceive(id, new Date());
    }

    @Override
    public List<StatisticsOrderDto> statisticsOrder(StatisticsVto statisticsVto) throws ParseException {
        if (statisticsVto.getDateListString() != null && !statisticsVto.getDateListString().equals("")) {
            //String 转 DateTime
            List<String> strings = (List<String>) statisticsVto.getDateListString();
            statisticsVto.setDateList(dateList(strings));
            Date startTime = statisticsVto.getDateList().get(0);
            Date endTime = statisticsVto.getDateList().get(1);
            //获取开始与结束时间之间的所有日期
            List<Date> dates = findDates(startTime, endTime);
            return add(dates, statisticsVto);
        } else {
            //获取当前时间
            Date dateNow = new Date();
            Date dateS = backDateTime(dateNow, 6);
            List<Date> dates = findDates(dateS, dateNow);
            return add(dates, statisticsVto);
        }
    }

    private List<StatisticsOrderDto> add(List<Date> dates, StatisticsVto statisticsVto) {
        List<StatisticsOrderDto> statisticsOrderDtos = new ArrayList<>();
        for (Date date : dates) {
            List<Date> dayEndStart = new ArrayList<>();
            statisticsVto.setStartTime(getStartTime(date));
            statisticsVto.setEndTime(getEndTime(date));
            StatisticsOrderDto statisticsOrderDto = orderMapper.StatisticsOrders(statisticsVto);
            statisticsOrderDto.setDateTime(date);
            dayEndStart.add(getStartTime(date));
            dayEndStart.add(getEndTime(date));
            statisticsOrderDto.setDateList(dayEndStart);
            statisticsOrderDtos.add(statisticsOrderDto);
        }
        return statisticsOrderDtos;
    }

    /**
     * 导出接口实现类
     * 导出文件存入指定位置，然后返回文件名称
     *
     * @param listVto
     * @return
     */
    @Override
    public void export(String merchantFile, ListVto listVto, HttpServletRequest request, HttpServletResponse response) throws IOException, ResultException {
        //根据条件查询SQL
        List<ExportDtoGoods> exportDtoGoods = orderMapper.exportDtoGoods(listVto);
        //生成需要生成的Excel的名字
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.currentTimeMillis());
        stringBuilder.append("订单导出文件.xlsx");
        String fileName = stringBuilder.toString();
        //将Excel写入文件夹中
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(fileName, "订单数据", ExcelType.XSSF),
                ExportDtoGoods.class, exportDtoGoods);
        //创建流，并写入内容
        FileOutputStream fileOutputStream = new FileOutputStream(merchantFile + fileName);
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        File.downLoad(request, response, fileName, merchantFile);
    }

}
