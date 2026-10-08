package com.takeOut.food.api.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.DownLoadDto;
import com.takeOut.food.api.bean.dto.OrderDto;
import com.takeOut.food.api.bean.dto.StatisticsOrderDto;
import com.takeOut.food.api.bean.general.result.Result;
import com.takeOut.food.api.bean.general.result.ResultException;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.bean.vto.OperationVto;
import com.takeOut.food.api.bean.vto.OrderPageVto;
import com.takeOut.food.api.bean.vto.StatisticsVto;
import com.takeOut.food.api.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @author

 */
public interface OrderService extends IService<Order> {

    Page<?> orderList(ListVto listVto);

    void operationOrder(OperationVto operationVto, Integer operationType) throws ResultException;

    List<Integer> orderCount(Integer id);

    Result orderPage(OrderPageVto orderPageVto);

    void updateStatus(Integer id, int i);

    OrderDto orderInfo(Integer id);

    void orderReceive(Integer id);

    List<StatisticsOrderDto> statisticsOrder(StatisticsVto statisticsVto) throws ParseException;

    void export(String merchantFile, ListVto listVto, HttpServletRequest request, HttpServletResponse response) throws IOException, ResultException;
}
