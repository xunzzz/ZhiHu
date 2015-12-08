package com.zx.zhihu.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangxun on 2015/9/21.
 * Gson的简单工具类
 */
public class GsonUtils {

    private static Gson gson = new Gson();

    /**
     * 将一个json字符串数据封装到一个javabean中
     *
     * @param result
     * @param clazz
     * @return
     */
    public static <T> T parse2Bean(String result, Class<T> clazz) {

        if (result != null) {
            if (gson == null) {
                gson = new Gson();
            }
            try {
                return gson.fromJson(result, clazz);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * 将一个对象封装成一个json字符串数据
     *
     * @param obj
     * @return
     */
    public static String getString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (gson == null) {
            gson = new Gson();
        }
        try {
            return gson.toJson(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * @param s      sample: message
     * @param clazz  sample: Product[].class
     * @return
     */
    public static <T> List<T> parse2List(String s, Class<T[]> clazz) throws JsonParseException {


        if (!StrUtils.isEmpty(s)) {
            T[] arr = new Gson().fromJson(s, clazz);
            return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
        }
        return null;
    }

}
