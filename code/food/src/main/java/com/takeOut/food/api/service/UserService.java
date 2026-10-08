package com.takeOut.food.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.LoginDto;
import com.takeOut.food.api.bean.general.result.ResultException;
import com.takeOut.food.api.bean.vto.AppUserVto;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.bean.vto.LoginVto;
import com.takeOut.food.api.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author @author

 */
public interface UserService extends IService<User> {

    LoginDto login(LoginVto loginVto) throws ResultException;

    void register(User user) throws ResultException;

    Page<?> userList(ListVto listVto) throws ResultException;

    void operationUser(User user, Integer operationType) throws ResultException;

    void isLogin() throws ResultException;

    User appUserUpdate(AppUserVto appUserVto);

    void registerAppUser(User user) throws ResultException;

    void updateUserInfo(User user) throws ResultException;
}
