package forpleuvoir.baka.utils;

import com.google.gson.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.List;

/**
 * json工具类
 *
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.util
 * @class_name JsonUtil
 * @create_time 2020/11/10 21:46
 */

public class JsonUtil {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static StringBuffer toStringBuffer(Object obj) {
        return new StringBuffer(gson.toJson(obj));
    }


    /**
     * 将对象转换成json字符串
     *
     * @param json 需要转换的对象
     * @return json字符串
     */
    public static String toJsonStr(Object json) {
        return gson.toJson(json);
    }


    /**
     * 将json字符串转换成JsonObject
     *
     * @param json 需要转换的字符串对象
     * @return JsonObject对象
     * @throws Exception
     */
    public static JsonObject strToJsonObject(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(JsonElement jsonElement, Type type) {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapterFactory(DataTypeAdaptor.FACTORY);
        Gson gson = gsonBuilder.create();
        Field field = ReflectionUtil.getFieldValueByName("factories", gson);
        try {
            assert field != null;
            List<TypeAdapterFactory> factories = new ArrayList<>(((List<TypeAdapterFactory>) field.get(gson)));
            factories.remove(1);
            ReflectionUtil.setFieldValueByName("factories", gson, factories);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return gson.fromJson(jsonElement, type);
    }
}
