package com.takeOut.food.api.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 分类实体
 * </p>
 *
 * @author @author

 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Class implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父级
     */
    @TableField(value = "parent_id", updateStrategy = FieldStrategy.IGNORED)
    private Integer parentId;

    /**
     * 图片url
     */
    @TableField(value = "pic_url", updateStrategy = FieldStrategy.IGNORED)
    private String picUrl;


    @TableField(exist = false)
    private List<Class> children;


}
