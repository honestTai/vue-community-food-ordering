package com.takeOut.food.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.GoodsDto;
import com.takeOut.food.api.bean.general.result.ResultException;
import com.takeOut.food.api.bean.general.result.ResultStatus;
import com.takeOut.food.api.bean.vto.GoodsPageAppVto;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.entity.Evaluation;
import com.takeOut.food.api.entity.Goods;
import com.takeOut.food.api.entity.User;
import com.takeOut.food.api.mapper.EvaluationMapper;
import com.takeOut.food.api.mapper.GoodsMapper;
import com.takeOut.food.api.mapper.OrderMapper;
import com.takeOut.food.api.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.takeOut.food.util.UserLocal;
import com.takeOut.food.util.BaseFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.takeOut.food.constant.operationConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @author

 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    EvaluationMapper evaluationMapper;

    /**
     * 分页查询
     * 餐品名字模糊查询，公司id查询，分类查询
     * 判断角色如果是ADMIN,可以进行公司ID查询
     * jsonString 转 JSON
     * 手写SQL查询
     *
     * @param listVto
     * @return
     */
    @Override
    public Page<?> goodsList(ListVto listVto) {
        User user = UserLocal.getUser();
        Page<GoodsDto> page = new Page<>(listVto.getPage(), listVto.getPageSize());
        IPage<GoodsDto> goodsDtoIPage = goodsMapper.selectGoodsByPage(page, listVto);
        //循环数据转List<Java>
        goodsDtoIPage.getRecords().forEach(goodsDto -> {
            if (!goodsDto.getGoods().getImages().equals("")) {
                goodsDto.getGoods().setGoodsPhotoList(BaseFunction.stringToList(goodsDto.getGoods().getImages()));
            }
        });
        //图片转换
        return (Page<GoodsDto>) goodsDtoIPage;
    }

    @Override
    public Page<?> goodsPageByApp(GoodsPageAppVto goodsPageAppVto) {
        Page<GoodsDto> page = new Page<>(goodsPageAppVto.getCurrent(), goodsPageAppVto.getSize());
        IPage<GoodsDto> goodsDtoIPage = goodsMapper.selectGoodsByPageApp(page, goodsPageAppVto);
        goodsDtoIPage.getRecords().stream().forEach(goodsDto -> {
            if (!goodsDto.getGoods().getImages().equals("")) {
                goodsDto.getGoods().setGoodsPhotoList(BaseFunction.stringToList(goodsDto.getGoods().getImages()));
            }
        });
        return (Page<?>) goodsDtoIPage;
    }

    @Override
    public GoodsDto goodsDetail(Integer goodsId) {
        GoodsDto goods = goodsMapper.selectGoodsByAppFromId(goodsId);
        if (!goods.getGoods().getImages().equals("")) {
            goods.getGoods().setGoodsPhotoList(BaseFunction.stringToList(goods.getGoods().getImages()));
        }
        goods.setCommentTotal(evaluationMapper.selectCount(new QueryWrapper<Evaluation>().eq("goods_id", goodsId)));
        return goods;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationGoods(Goods goods, Integer operationType) throws ResultException {
        if (goods.getGoodsPhotoList() != null) {
            goods.setImages(goods.getGoodsPhotoList().size() != 0 ? BaseFunction.listToString(goods.getGoodsPhotoList()) : "");
        }
        /**
         * 修改可以修改价格，修改分类，介绍，名称，规格，规格为JSON,转换
         * 增加需要将JSON转为jsonString增加
         */
        if (operationType == UPDATE) {
            goodsMapper.updateById(goods);
        } else if (operationType == DELETE) {
            /**
             * 执行删除
             * 判断相关订单状态（0与1 可以删除）
             * 删除对应的购物车-删除
             */
            boolean orderGoods = orderMapper.selectCountByGoodsId(goods.getId()) == 0;
            if (!orderGoods) {
                throw new ResultException(ResultStatus.ORDER_GOODS_USE);
            } else {
                //订单不删除，餐品删除
                goodsMapper.deleteById(goods);
            }
        } else if (operationType == ADD) {
            goodsMapper.insert(goods);
        }
    }

    //餐品减库存
    @Override
    public void updateStock(int i, Integer goodsId) {
        orderMapper.updateStock(i, goodsId);
    }

}
