package com.takeOut.food.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.CommentDto;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.bean.vto.AppPageVto;
import com.takeOut.food.api.entity.Evaluation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @author

 */
public interface EvaluationService extends IService<Evaluation> {

    Page<?> evaluaList(ListVto listVto);

    void operationEvaluation(Evaluation evaluation);

    Page<CommentDto> userCommentPage(AppPageVto appPageVto);

    Page<CommentDto> goodsCommentPage(AppPageVto appPageVto);
}
