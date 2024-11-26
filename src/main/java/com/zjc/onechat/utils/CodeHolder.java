package com.zjc.onechat.utils;

public class CodeHolder {
    private static final ThreadLocal<String> tl = new ThreadLocal<>();

    public static void saveCode(String code){
        tl.set(code);
    }

    public static String getCode(){
        return tl.get();
    }

    public static void removeCode(){
        tl.remove();
    }
}
