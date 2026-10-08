package com.takeOut.food.api.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.CommentDto;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.bean.vto.AppPageVto;
import com.takeOut.food.api.entity.Evaluation;
import com.takeOut.food.api.mapper.EvaluationMapper;
import com.takeOut.food.api.service.EvaluationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.takeOut.food.util.UserLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @author

 */
@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements EvaluationService {

    @Autowired
    EvaluationMapper evaluationMapper;

    /**
     * 根据餐品查询评价
     * 需要评价内容，评价用户名称，评价id,评价时间
     * 每页5页进行分页
     *
     * @param listVto
     * @return
     */
    @Override
    public Page<?> evaluaList(ListVto listVto) {
        return evaluationMapper.selectCommentListByPage(new Page<CommentDto>(listVto.getPage(), listVto.getPageSize()), listVto);
    }

    /**
     * 修改评论
     * 添加评价回复内容
     */
    @Override
    public void operationEvaluation(Evaluation evaluation) {
        evaluation.setReplyId(UserLocal.getUser().getId());
        evaluationMapper.addReplyContent(evaluation);
    }

    @Override
    public Page<CommentDto> userCommentPage(AppPageVto appPageVto) {
        return evaluationMapper.userCommentPage(new Page<CommentDto>(appPageVto.getCurrent(), appPageVto.getSize()), UserLocal.getUser().getId());
    }

    @Override
    public  Page<CommentDto> goodsCommentPage(AppPageVto appPageVto){
        return evaluationMapper.goodsCommentPage(new Page<CommentDto>(appPageVto.getCurrent(), appPageVto.getSize()), appPageVto.getGoodsId());
    }

}
