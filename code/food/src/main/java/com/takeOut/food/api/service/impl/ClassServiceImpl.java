package com.takeOut.food.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.entity.Class;
import com.takeOut.food.api.mapper.ClassMapper;
import com.takeOut.food.api.service.ClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.takeOut.food.util.Tree;
import com.takeOut.food.util.UserLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.takeOut.food.constant.operationConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @author

 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

    @Autowired
    ClassMapper classMapper;

    /**
     * 分页查询，带分类Id
     * 对查询结果进行递归，结果已经分页，因此递归对分页无影响
     * 为空判断查询父级
     *
     * @param listVto
     * @return
     */
    @Override
    public Page<?> classList(ListVto listVto) {
        Page<Class> page = new Page<>(listVto.getPage(), listVto.getPageSize());
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(listVto.getClassId() != null, "parent_id", listVto.getClassId());
        queryWrapper.or(listVto.getClassId() != null).eq(listVto.getClassId() != null, "id", listVto.getClassId());
        Page<Class> classIPage = classMapper.selectPage(page, queryWrapper);
        if (classIPage.getTotal() != 0) {
            classIPage.setRecords(Tree.buildByRecursive(classIPage.getRecords()));
            classIPage.setTotal(classIPage.getRecords().stream().filter(x -> x.getParentId() == null).count());
        }
        return classIPage;
    }

    @Override
    public void operationClass(Class aClass, Integer operationType) {
        //只能修改名称
        if (operationType == ADD) {
            classMapper.insert(aClass);
        } else if (operationType == UPDATE) {
            classMapper.updateById(aClass);
        } else if (operationType == DELETE) {
            classMapper.deleteById(aClass);
        }
    }

    @Override
    public List<Class> classTree() {
        List<Class> classList = classMapper.selectList(new QueryWrapper<>());
        return Tree.buildByRecursive(classList);
    }

}