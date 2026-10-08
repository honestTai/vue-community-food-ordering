package com.takeOut.food.api.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.GoodsDto;
import com.takeOut.food.api.bean.vto.GoodsPageAppVto;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author @author

 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    IPage<GoodsDto> selectGoodsByPage(Page<GoodsDto> page, ListVto listVto);

    IPage<GoodsDto> selectGoodsByPageApp(Page<GoodsDto> page, GoodsPageAppVto goodsPageAppVto);

    GoodsDto selectGoodsByAppFromId(Integer goodsId);
}
