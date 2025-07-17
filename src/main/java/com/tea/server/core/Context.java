package com.tea.server.core;



import com.tea.server.entity.Users;

import java.util.Optional;

public class Context {

    private static final ThreadLocal<Users> USER = new ThreadLocal<>();

    public static Long getUserId(){
        return Optional.ofNullable(USER.get()).map(Users::getId).orElse(-1L);
    }
    public static Users getUser(){
        return Optional.ofNullable(USER.get()).orElse(null);
    }

    public static void setUser(Users user){
        USER.set(user);
    }

}