package com.takeOut.food.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.GoodsDto;
import com.takeOut.food.api.bean.general.result.ResultException;
import com.takeOut.food.api.bean.vto.GoodsPageAppVto;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @author

 */
public interface GoodsService extends IService<Goods> {

    Page<?> goodsList(ListVto listVto);

    void operationGoods(Goods goods, Integer operationType) throws ResultException;

    Page<?> goodsPageByApp(GoodsPageAppVto listVto);

    GoodsDto goodsDetail(Integer goodsId);

    void updateStock(int i,Integer goodsId);
}
