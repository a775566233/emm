package com.emm.entity.token;

import com.emm.entity.User;

public class UserTokenThreadLocal {
    //存储用户信息
    private static final ThreadLocal<TokenInfo<User>> userThreadLocal = new ThreadLocal<>();

    public static void set(TokenInfo<User> user) {
        userThreadLocal.set(user);
    }

    public static TokenInfo<User> get() {
        return userThreadLocal.get();
    }

    public static void remove() {
        userThreadLocal.remove();
    }
}
