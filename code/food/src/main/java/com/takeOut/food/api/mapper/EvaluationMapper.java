package com.takeOut.food.api.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.CommentDto;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.entity.Evaluation;
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
public interface EvaluationMapper extends BaseMapper<Evaluation> {

    Page<?> selectCommentListByPage(Page<CommentDto> commentDtoPage, ListVto listVto);

    void addReplyContent(Evaluation evaluation);

    Page<CommentDto> userCommentPage(Page<CommentDto> commentDtoPage, Integer id);

    Page<CommentDto> goodsCommentPage(Page<CommentDto> commentDtoPage, Integer goodsId);
}
