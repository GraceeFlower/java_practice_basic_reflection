package com.thoughtworks.util;

import com.thoughtworks.annotation.Limit;

import java.lang.reflect.Field;

public final class LimitValidator {
    private LimitValidator() { }


    /**
     * 检查被Limit注解标注的字段是否符合Limit的限制
     * 只对int类型的字段生效
     */
    public static void validate(Object object) throws IllegalAccessException {
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Limit.class)) {
                Limit annotation = declaredField.getAnnotation(Limit.class);

                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }

                if (int.class.isAssignableFrom(declaredField.getType())) {
                    int min = annotation.min();
                    int max = annotation.max();

                    final int intValue = declaredField.getInt(object);
                    if (intValue < min || intValue > max) {
                        throw new RuntimeException(String.format("[%s]对象的[%s]字段不符合Limit要求", object.getClass(), declaredField.getName()));
                    }
                }
            }
        }
    }
}
