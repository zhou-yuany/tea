package com.tea.server.core;





import com.tea.server.entity.Admins;

import java.util.Optional;

public class Context {

    private static final ThreadLocal<Admins> USER = new ThreadLocal<>();

    public static Long getUserId(){
        return Optional.ofNullable(USER.get()).map(Admins::getId).orElse(-1L);
    }
    public static Admins getUser(){
        return Optional.ofNullable(USER.get()).orElse(null);
    }

    public static void setUser(Admins user){
        USER.set(user);
    }

}