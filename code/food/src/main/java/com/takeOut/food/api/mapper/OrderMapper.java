package com.takeOut.food.api.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.ExportDtoGoods;
import com.takeOut.food.api.bean.dto.OrderDto;
import com.takeOut.food.api.bean.dto.StatisticsOrderDto;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.bean.vto.OrderPageVto;
import com.takeOut.food.api.bean.vto.StatisticsVto;
import com.takeOut.food.api.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author @author

 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    IPage<OrderDto> selectByPage(Page<OrderDto> page, ListVto listVto);

    int selectCountByGoodsId(Integer goodsId);

    Integer selectCountByLoginUser(Integer id, int i);

    void updateStock(int i,Integer goodsId);

    IPage<OrderDto> orderPage(Page<OrderDto> orderDtoPage, OrderPageVto orderPageVto);

    void updateStatus(Integer id, int i);

    OrderDto orderInfo(Integer id);

    //订单收货
    void orderReceive(Integer id, Date date);

    StatisticsOrderDto StatisticsOrders(StatisticsVto statisticsVto);

    List<ExportDtoGoods> exportDtoGoods(ListVto listVto);
}
