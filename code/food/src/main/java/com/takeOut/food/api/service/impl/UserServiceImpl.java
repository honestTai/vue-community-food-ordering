package com.takeOut.food.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.takeOut.food.api.bean.dto.LoginDto;
import com.takeOut.food.api.bean.general.result.ResultException;
import com.takeOut.food.api.bean.general.result.ResultStatus;
import com.takeOut.food.api.bean.vto.AppUserVto;
import com.takeOut.food.api.bean.vto.ListVto;
import com.takeOut.food.api.bean.vto.LoginVto;
import com.takeOut.food.api.entity.User;
import com.takeOut.food.api.mapper.UserMapper;
import com.takeOut.food.api.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.takeOut.food.constant.SecurityConstants;
import com.takeOut.food.util.JwtUtils;
import com.takeOut.food.util.RedisUtil;
import com.takeOut.food.util.UserLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.takeOut.food.constant.RoleConstants.*;
import static com.takeOut.food.constant.operationConstants.*;
import static com.takeOut.food.util.encryption.MD5Util.getMD5;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author @author
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public LoginDto login(LoginVto loginVto) throws ResultException {
        // 用户登录认证
        return authLogin(loginVto);
    }

    @Override
    public void register(User user) throws ResultException {
        if (userMapper.selectOne(new QueryWrapper<User>().eq("number", user.getNumber())) != null) {
            throw new ResultException(ResultStatus.NUM);
        }
        user.setType(ADMIN);
        user.setTime(new Date());
        user.setPassword(bCryptPasswordEncoder.encode(getMD5(user.getPassword())));
        userMapper.insert(user);
    }

    /**
     * 用户登录认证
     *
     * @param loginVto 用户登录信息
     */
    private LoginDto authLogin(LoginVto loginVto) throws ResultException {
        String userName = loginVto.getNumber();
        String password = getMD5(loginVto.getPassword());

        // 根据登录账号获取用户信息
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("number", userName));
        if (user == null) {
            throw new ResultException(ResultStatus.ERROR_NUM_PWD);
        }
        // 验证登录密码是否正确。如果正确，则赋予用户相应权限并生成用户认证信息
        if (this.bCryptPasswordEncoder.matches(password, user.getPassword())) {
            if (user.getType().equals(NOLOGIN)) {
                throw new ResultException(ResultStatus.NOT_PASS_LOGIN);
            }
            List<String> roleList = new ArrayList<>();
            roleList.add("ROLE_USER");
            // 生成 token
            String token = JwtUtils.generateToken(userName, roleList, false);

            // 认证成功后，设置认证信息到 Spring Security 上下文中
            Authentication authentication = JwtUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //注入当前用户的基本信息
            UserLocal.setUser(user);
            return new LoginDto(user, SecurityConstants.TOKEN_PREFIX + token);
        }
        throw new ResultException(ResultStatus.ERROR_NUM_PWD);
    }

    @Override
    public Page<?> userList(ListVto listVto) throws ResultException {
        return userMapper.selectUserByPage(new Page<>(listVto.getPage(), listVto.getPageSize()), listVto);
    }

    /**
     * @param user
     * @param operationType 0禁止登录1重置密码2允许登录3修改信息4添加用户
     * @throws ResultException
     */
    @Override
    @Transactional
    public void operationUser(User user, Integer operationType) throws ResultException {
        User dbUser = userMapper.selectById(user.getId());
        int status = 1;
        if (Objects.equals(operationType, USER_NO_LOGIN)) {
            user.setType(NOLOGIN);
            //修改状态
            userMapper.updateById(user);
        } else if (Objects.equals(operationType, USER_LOGIN)) {

            user.setType(CUSTOMER);

            userMapper.updateById(user);
        } else if (Objects.equals(operationType, USER_PASSWORD)) {
            user.setPassword(bCryptPasswordEncoder.encode(getMD5("123456")));
            userMapper.updateById(user);
        } else if (Objects.equals(operationType, USER_UPDATE)) {
            user.setType(dbUser.getType());
            if (!user.getPassword().equals(dbUser.getPassword())) {
                //密码修改
                user.setPassword(bCryptPasswordEncoder.encode(getMD5(user.getPassword())));
                status = 2;
            }
            if (!user.getNumber().equals(dbUser.getNumber())) {
                if (userMapper.selectOne(new QueryWrapper<User>().eq("number", user.getNumber())) != null) {
                    throw new ResultException(ResultStatus.NUM);
                }
            }
            //直接修改
            userMapper.updateById(user);
            if (status == 2) {
                //强制退出，重新登录
                SecurityContextHolder.clearContext();
                throw new ResultException(ResultStatus.NO_LOGIN);
            }
        } else if(Objects.equals(operationType, USER_DELETE)){
            removeById(user.getId());
        }else {
            //新增走注册接口
            register(user);
        }
    }


    @Override
    public User appUserUpdate(AppUserVto appUserVto) {
        User user = UserLocal.getUser();
        assert user != null;
        user.setSex(appUserVto.getGender());
        user.setHeadimgUrl(appUserVto.getAvatarUrl());
        user.setName(appUserVto.getNickName());
        userMapper.updateById(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerAppUser(User user) throws ResultException {
        if (userMapper.selectOne(new QueryWrapper<User>().eq("number", user.getNumber())) != null) {
            throw new ResultException(ResultStatus.NUM);
        }
        user.setTime(new Date());
        user.setPassword(bCryptPasswordEncoder.encode(getMD5(user.getPassword())));
        userMapper.insert(user);
    }

    @Override
    public void updateUserInfo(User user) throws ResultException {
        User dbUser = UserLocal.getUser();
        assert dbUser != null;
        if (!dbUser.getPassword().equals(user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(getMD5(user.getPassword())));
        }
        if (!dbUser.getNumber().equals(user.getNumber())) {
            if (userMapper.selectOne(new QueryWrapper<User>().eq("number", user.getNumber())) != null) {
                throw new ResultException(ResultStatus.NUM);
            }
        }
        userMapper.updateById(user);
    }


    @Override
    public void isLogin() throws ResultException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new ResultException(ResultStatus.NO_LOGIN);
        }
    }

}
