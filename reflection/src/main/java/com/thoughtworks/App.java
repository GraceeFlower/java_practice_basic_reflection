package com.thoughtworks;

import com.thoughtworks.annotation.Alias;
import com.thoughtworks.constant.Gender;
import com.thoughtworks.model.Animal;
import com.thoughtworks.model.Desk;
import com.thoughtworks.model.JsonModel;
import com.thoughtworks.model.Parrot;
import com.thoughtworks.model.Walkable;
import com.thoughtworks.util.JsonUtil;
import com.thoughtworks.util.LimitValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.util.Arrays.asList;

public class App {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        // class对象获取: 三种方法
        // Grace: class com.thoughtworks.model.Parrot
        // 为什么有个 class 在前面
        System.out.println(getParrotClass());
        System.out.println("=======================");

        // newInstance方法 只能调用对应类的共有无参方法
        // Grace: Parrot{flySpeed=0, canTalk=false}
        // 下面两种方式结果相同，成员变量为无参构造之后的默认值
        Parrot parrot = Parrot.class.newInstance();
        System.out.println(parrot);
        Parrot parrot2 = new Parrot();
        System.out.println(parrot2);
        System.out.println("=======================");

        // 获取构造器
        getConstructor();
        System.out.println("=======================");

        // 使用构造器
        useConstructor();
        System.out.println("=======================");

        // 获取字段
        getField();
        System.out.println("=======================");

        // 常见的反射字段使用:获取对应字段的值
        useField();
        System.out.println("=======================");

        // 获取方法
        getMethod();
        System.out.println("=======================");

        // 常见的反射方法使用:调用对应的方法
        useMethod();
        System.out.println("=======================");

        // 获取注解
        getAnnotation();
        System.out.println("=======================");

        // 其他常用方法
        otherReflection();

        // 应用1: 使Limit注解功能生效
        limitExample();

        // 应用2: 把对象转换成json字符串
        jsonExample();
    }

    private static void jsonExample() {
        Parrot parrot = new Parrot();
        parrot.setFlySpeed(100);
        parrot.setCanTalk(true);
        parrot.petName = "wuwu";

        System.out.println(JsonUtil.toJson(parrot));

        final JsonModel jsonModel = new JsonModel("Lily",
                20,
                new Parrot(),
                asList("a", "b"),
                asList(new Parrot(true), parrot),
                asList(new Desk(10), new Desk(20)));

        System.out.println(JsonUtil.toJson(jsonModel));
    }

    private static void limitExample() throws IllegalAccessException {
        Desk desk1 = new Desk(100);
        Desk desk2 = new Desk(-1);

        Parrot parrot1 = new Parrot();
        parrot1.setFlySpeed(100);
        Parrot parrot2 = new Parrot();
        parrot2.setFlySpeed(200);

        LimitValidator.validate(desk1);
        LimitValidator.validate(parrot1);

        // 报错
        // LimitValidator.validate(desk2);
        // LimitValidator.validate(parrot2);
    }

    private static void otherReflection() {
        // 判断某个类是不是指定接口或者父类的实现类或者子类 isAssignableFrom 方法
        Walkable.class.isAssignableFrom(Parrot.class); // true
        Walkable.class.isAssignableFrom(Animal.class); // true
        Parrot.class.isAssignableFrom(Parrot.class); // true

        // 获取父类 注意 只能获取直接父类
        Class<? super Parrot> superclass = Parrot.class.getSuperclass();
        Class<Animal> animalClass = (Class<Animal>) superclass;

        // 获取接口 注意只能获取到直接实现的接口
        Class<?>[] interfaces = Animal.class.getInterfaces(); // 数组里只有一个Walkable
        final Class<?>[] interfaces1 = Parrot.class.getInterfaces(); // 空数组
    }

    private static void getAnnotation() throws NoSuchFieldException {
        Class<Parrot> parrotClass = Parrot.class;
        // 获取指定的annotation
        if (parrotClass.isAnnotationPresent(Alias.class)) {
            Alias alias = parrotClass.getAnnotation(Alias.class);
            // 获取annotation的属性
            String value = alias.value();
        }

        // 获取所有annotation
        Annotation[] annotations = parrotClass.getAnnotations();

        // Grace: test
        System.out.println(Arrays.toString(annotations));
    }

    private static void useMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<Parrot> parrotClass = Parrot.class;
        Parrot parrot = new Parrot();

        Method setCanTalkMethod = parrotClass.getDeclaredMethod("setCanTalk", boolean.class);
        // 先判断可见性
        if (!setCanTalkMethod.isAccessible()) {
            setCanTalkMethod.setAccessible(true);
        }
        // 通过invoke方法执行 跟调用字段值一样 同样需要对应的实例 由于是方法调用 所以当然也需要参数
        setCanTalkMethod.invoke(parrot, true);

        // Grace: test
        System.out.println(setCanTalkMethod);
    }

    private static void getMethod() throws NoSuchMethodException {
        Class<Parrot> parrotClass = Parrot.class;

        // 根据方法名和参数列表获取方法 getMethod方法只能获取public的方法(包括继承的public方法)
        Method setCanTalk = parrotClass.getMethod("setCanTalk", boolean.class);

        // 获取所有pubic方法 包括从父类继承的public方法
        // Grace: 还有很多内置方法，例如hashCode之类的
        Method[] methods = parrotClass.getMethods();

        // 根据方法名和参数列表获取方法 getDeclaredMethod方法只能获取自身的方法(包括private方法)
        Method privateMethod = parrotClass.getDeclaredMethod("privateMethod");

        // 获取所有自身方法 包括private方法
        // Grace: 是所有自身的方法哦～！
        Method[] declaredMethods = parrotClass.getDeclaredMethods();

        // Grace: test
        System.out.println(setCanTalk + "\n" + Arrays.toString(methods)
            + "\n" + privateMethod + "\n" + Arrays.toString(declaredMethods));
    }

    private static void useField() throws NoSuchFieldException, IllegalAccessException {
        Class<Parrot> parrotClass = Parrot.class;
        Parrot parrot = new Parrot();
        parrot.setCanTalk(true);
        parrot.petName = "wuwu";

        Field petName = parrotClass.getDeclaredField("petName");
        // 在字段返回值结果未知的情况下可以使用getType方法判断返回值类型
        Class<?> petNameType = petName.getType();
        if (petNameType.isAssignableFrom(String.class)) {
            // 使用get方法获取Parrot实例的值 注意获取字段的值必须要有一个实例 很好理解 如果没有实例 字段哪里来的值?
            String s = (String) petName.get(parrot);
        }

        Field canTalkField = parrotClass.getDeclaredField("canTalk");
        // 私有字段先设置可见性为true 如果字段可见性位置可以先判断
        if (!canTalkField.isAccessible()) {
            canTalkField.setAccessible(true);
        }
        // 在知道字段的类型的情况下可以直接get,如果该类型是基本类型则直接使用getXXX方法
        boolean canTalk = canTalkField.getBoolean(parrot);

        // Grace: test
        System.out.println(petName + "\n" + petNameType + "\n" + canTalkField + "\n" + canTalk);
    }

    private static void getField() throws NoSuchFieldException {
        Class<Parrot> parrotClass = Parrot.class;

        // 根据字段名获取字段 getField方法只能获取public的字段(包括继承的public字段)
        Field petNameField = parrotClass.getField("petName");
        // 获取所有public的字段 包括从父类继承的public字段
        Field[] fields = parrotClass.getFields();

        // 根据字段名获取字段 getDeclaredField只能获取自身的字段(包括private字段)
        Field canTalkField = parrotClass.getDeclaredField("canTalk");
        // 获取自身所有字段 包括private字段
        Field[] declaredFields = parrotClass.getDeclaredFields();

        System.out.println(petNameField + "\n" + Arrays.toString(fields)
            + "\n" + canTalkField
            + "\n" + Arrays.toString(declaredFields));
    }

    private static void useConstructor() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Parrot> parrotClass = Parrot.class;
        Constructor<Parrot>[] declaredConstructors = (Constructor<Parrot>[]) parrotClass.getDeclaredConstructors();

        // 调用无参构造器
        Parrot parrot = declaredConstructors[2].newInstance();

        // 调用有boolean类型的构造器
        Parrot parrot1 = declaredConstructors[1].newInstance(true);

        // 调用private的int,boolean类型的构造器
        // 先使private的构造器可以被反射调用
        declaredConstructors[0].setAccessible(true);
        // 再调用该构造器
        declaredConstructors[0].newInstance(5, true);

        // Grace: test
        System.out.println(parrot + "\n" + parrot1
            + "\n" + declaredConstructors[0].newInstance(5, true));
    }

    private static void getConstructor() throws NoSuchMethodException {
        Class<Parrot> parrotClass = Parrot.class;

        // 根据参数类型获取public构造方法
        Constructor<Parrot> constructor = parrotClass.getConstructor(boolean.class);
        // Grace: 下面这种不能直接获取，只有自己的属性可以这样获取，父类的属性不能这么拿到
        // Constructor<Parrot> constructor2 = parrotClass.getConstructor(int.class);
        Constructor<Parrot> constructor1 = parrotClass.getConstructor(); //无参构造器
        // 尝试获取private构造方法 报错
        // Constructor<Parrot> constructor1 = parrotClass.getConstructor(int.class, boolean.class);

        // 获取所有该类所有public的构造方法
        Constructor<?>[] constructors = parrotClass.getConstructors();

        // getDeclaredConstructor方法可以获取私有构造方法
        Constructor<Parrot> declaredConstructor = parrotClass.getDeclaredConstructor(int.class, boolean.class);
        // 获取所有构造方法 包括非public的
        Constructor<?>[] declaredConstructors = parrotClass.getDeclaredConstructors();

        // 枚举无法通过反射获取构造方法 enumConstructors为空数组
        Constructor<?>[] enumConstructors = Gender.class.getConstructors();

        // Grace: test
        System.out.println(constructor + "\n" + constructor1
            + "\n" + Arrays.toString(constructors)
            + "\n" +declaredConstructor
            + "\n" + Arrays.toString(declaredConstructors)
            + "\n" + Arrays.toString(enumConstructors));

    }

    private static Class<Parrot> getParrotClass() throws ClassNotFoundException {
        // 类名.class
        Class<Parrot> parrotClass = Parrot.class;

        // 对象.getClass()方法
        Parrot parrot = new Parrot();
        Class<? extends Parrot> aClass = parrot.getClass();

        // Class.forName(全限定名)
        Class<Parrot> aClass1 = (Class<Parrot>) Class.forName("com.thoughtworks.model.Parrot");
        return aClass1;
    }

}
