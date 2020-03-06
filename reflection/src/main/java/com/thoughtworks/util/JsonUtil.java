package com.thoughtworks.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class JsonUtil {
    private JsonUtil() {
    }

    private static final String STRING_WITH_QUOTES = "\"%s\"";
    private static final String COLON = ":";
    private static final String COMMA = ",";

    /**
     * 把一个对象转换成json字符串
     * 只转换该对象的所有字段
     */
    public static String toJson(Object object)  {
        if (object == null) {
            return null;
        }

        final Class<?> fieldType = object.getClass();
        // 判断是不是基本类型及其包装类型 直接返回结果
        if (fieldType.isPrimitive() || Number.class.isAssignableFrom(fieldType) || Boolean.class == fieldType) {
            return object.toString();
        }

        // String类型直接返回
        if (fieldType == String.class) {
            return String.format(STRING_WITH_QUOTES, object);
        }

        final Field[] declaredFields = object.getClass().getDeclaredFields();

        Map<String, Object> jsonMap = new HashMap<>();
        for (Field declaredField : declaredFields) {
            final String key = String.format(STRING_WITH_QUOTES, declaredField.getName());
            Object value = getValue(object, declaredField);
            jsonMap.put(key, value);
        }

        final String jsonString = jsonMap.entrySet().stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .map(entry -> entry.getKey() + COLON + entry.getValue())
                .collect(Collectors.joining(COMMA));
        return "{" + jsonString + "}";
    }

    private static Object getValue(Object object, Field declaredField) {
        if (!declaredField.isAccessible()) {
            declaredField.setAccessible(true);
        }

        final Object fieldValue = getObject(object, declaredField);
        final Class<?> fieldType = declaredField.getType();

        // 可迭代类型
        if (Iterable.class.isAssignableFrom(fieldType)) {
            Iterable<?> iterable = (Iterable<?>) fieldValue;
            final String collectString = StreamSupport.stream(iterable.spliterator(), false)
                    .map(JsonUtil::toJson)
                    .collect(Collectors.joining(COMMA));
            return "[" + collectString +"]";
        }
        // 其他包装类型
        return toJson(fieldValue);
    }

    private static Object getObject(Object object,Field declaredField) {
        try {
            return declaredField.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取字段失败");
        }
    }

    /**
     * 把字符串转换成对应的对象
     * 字符串格式为toJson方法生成的字符串格式
     * 简单版本: 能够正确反序列化Parrot对象
     * 进阶版本: 能够正确反序列化JsonModel对象
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return null;
    }
}
