package com.takeOut.food.api.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.entity.Class;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @author

 */
public interface ClassService extends IService<Class> {

    Page<?> classList(ListVto listVto);

    void operationClass(Class aClass, Integer operationType);

    List<Class> classTree();
}
