package com.takeOut.food.util;

import com.takeOut.food.api.entity.User;

/**
 * 用户本地守护线程类
 */
public class UserLocal {

    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();

    /**
     * 设置用户信息
     */
    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public static User getUser() {
        return userThreadLocal.get() == null ? null : userThreadLocal.get();
    }
}
