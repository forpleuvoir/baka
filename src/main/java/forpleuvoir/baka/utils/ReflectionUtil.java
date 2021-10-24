package forpleuvoir.baka.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具
 *
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.Reflection
 * @class_name ReflectionUtil
 * @create_time 2020/11/10 18:25
 */

public class ReflectionUtil {
    public static List<Class<?>> getSuperClass(Class<?> clazz){
        List<Class<?>> clazzs=new ArrayList<>();
        Class<?> suCl=clazz.getSuperclass();
        clazzs.add(clazz.getSuperclass());
        while(suCl!=null){
            clazzs.add(suCl);
            suCl=suCl.getSuperclass();
        }

        return clazzs;
    }

    public static Object getPrivateFieldValueByType(Object o, Class<?> objectClasstype, Class<?> fieldClasstype) {
        return getPrivateFieldValueByType(o, objectClasstype, fieldClasstype, 0);
    }

    public static Object getPrivateFieldValueByType(Object o, Class<?> objectClasstype, Class<?> fieldClasstype, int index) {
        Class objectClass;
        if (o != null) {
            objectClass = o.getClass();
        } else {
            objectClass = objectClasstype;
        }

        while(!objectClass.equals(objectClasstype) && objectClass.getSuperclass() != null) {
            objectClass = objectClass.getSuperclass();
        }

        int counter = 0;
        Field[] fields = objectClass.getDeclaredFields();

        for(int i = 0; i < fields.length; ++i) {
            if (fieldClasstype.equals(fields[i].getType())) {
                if (counter == index) {
                    try {
                        fields[i].setAccessible(true);
                        return fields[i].get(o);
                    } catch (IllegalAccessException var9) {
                    }
                }

                ++counter;
            }
        }

        return null;
    }

    public static void setPrivateFieldValueByName(Object o, String fieldName,Object value){
        Field[] fields = o.getClass().getDeclaredFields();
        for(int i = 0; i < fields.length; ++i) {
            if (fieldName.equals(fields[i].getName())) {
                try {
                    fields[i].setAccessible(true);
                    fields[i].set(o,value);
                } catch (IllegalAccessException var5) {
                }
            }
        }
    }

    public static Object getPrivateFieldValueByName(Object o, String fieldName){
        Field[] fields = o.getClass().getDeclaredFields();
        for(int i = 0; i < fields.length; ++i) {
            if (fieldName.equals(fields[i].getName())) {
                try {
                    fields[i].setAccessible(true);
                    return fields[i].get(o);
                } catch (IllegalAccessException var5) {
                }
            }
        }
        return null;
    }

    public static Object getFieldValueByName(Object o, String fieldName) {
        Field[] fields = o.getClass().getFields();

        for(int i = 0; i < fields.length; ++i) {
            if (fieldName.equals(fields[i].getName())) {
                try {
                    fields[i].setAccessible(true);
                    return fields[i].get(o);
                } catch (IllegalAccessException var5) {
                }
            }
        }

        return null;
    }

    public static ArrayList<Field> getFieldsByType(Object o, Class<?> objectClassBaseType, Class<?> fieldClasstype) {
        ArrayList<Field> matches = new ArrayList();

        for(Class objectClass = o.getClass(); !objectClass.equals(objectClassBaseType) && objectClass.getSuperclass() != null; objectClass = objectClass.getSuperclass()) {
            Field[] fields = objectClass.getDeclaredFields();

            for(int i = 0; i < fields.length; ++i) {
                if (fieldClasstype.isAssignableFrom(fields[i].getType())) {
                    fields[i].setAccessible(true);
                    matches.add(fields[i]);
                }
            }
        }

        return matches;
    }

    public static Field getFieldByType(Object o, Class<?> objectClasstype, Class<?> fieldClasstype) {
        return getFieldByType(o, objectClasstype, fieldClasstype, 0);
    }

    public static Field getFieldByType(Class<?> objectClasstype, Class<?> fieldClasstype, int index) {
        int counter = 0;
        Field[] fields = objectClasstype.getDeclaredFields();
        for(int i = 0; i < fields.length; ++i) {
            if (fieldClasstype.equals(fields[i].getType())) {
                if (counter == index) {
                    fields[i].setAccessible(true);
                    return fields[i];
                }
                ++counter;
            }
        }
        return null;
    }
    public static Field getFieldByType(Object o, Class<?> objectClasstype, Class<?> fieldClasstype, int index) {
        Class objectClass;
        for(objectClass = o.getClass(); !objectClass.equals(objectClasstype) && objectClass.getSuperclass() != null; objectClass = objectClass.getSuperclass()) {
        }

        int counter = 0;
        Field[] fields = objectClass.getDeclaredFields();

        for(int i = 0; i < fields.length; ++i) {
            if (fieldClasstype.equals(fields[i].getType())) {
                if (counter == index) {
                    fields[i].setAccessible(true);
                    return fields[i];
                }

                ++counter;
            }
        }

        return null;
    }

    public static Method getMethodByType(Class<?> objectType, Class<?> returnType, Class<?>... parameterTypes) {
        return getMethodByType(0, objectType, returnType, parameterTypes);
    }

    public static Method getMethodByType(int index, Class<?> objectType, Class<?> returnType, Class<?>... parameterTypes) {
        Method[] methods = objectType.getDeclaredMethods();
        int counter = 0;

        for(int i = 0; i < methods.length; ++i) {
            if (returnType.equals(methods[i].getReturnType())) {
                Class<?>[] methodParameterTypes = methods[i].getParameterTypes();
                if (parameterTypes.length == methodParameterTypes.length) {
                    boolean match = true;

                    for(int t = 0; t < parameterTypes.length; ++t) {
                        if (parameterTypes[t] != methodParameterTypes[t]) {
                            match = false;
                        }
                    }

                    if (counter == index && match) {
                        methods[i].setAccessible(true);
                        return methods[i];
                    }
                }

                ++counter;
            }
        }

        return null;
    }

    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException var2) {
            return false;
        }
    }
    /**
     * 获取包名
     *
     * @return 包名
     */
    public static String getPackage(Class<?> clazz) {
        Package pck = clazz.getPackage();
        if (null != pck) {
            return pck.getName();
        } else {
            return "";
        }
    }

    /**
     * 获取继承的父类的全类名
     *
     * @return 继承的父类的全类名
     */
    public static String getSuperClassName(Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        if (null != superClass) {
            return superClass.getName();
        } else {
            return "没有父类！";
        }
    }

    /**
     * 获取全类名
     *
     * @return 全类名【String类型】
     */
    public static String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * 获取实现的接口名
     *
     * @return 所有的接口名【每一个接口名的类型为String，最后保存到一个List集合中】
     */
    public static List<String> getInterfaces(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        int len = interfaces.length;

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            Class<?> itfc = interfaces[i];

            // 接口名
            String interfaceName = itfc.getSimpleName();

            list.add(interfaceName);
        }

        return list;
    }


    /**
     * 获取类中包含特定注解属性
     *
     * @param clazz      类
     * @param annotation 注解
     * @return 属性集合
     */
    public static List<Field> getFieldByAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> list = new ArrayList<>();
        for (Field f : fields) {
            if (f.getAnnotation(annotation) != null) {
                list.add(f);
            }
        }
        return list;
    }

    /**
     * 修改对象的属性值
     *
     * @param fieldName 属性名
     * @param object    对象
     * @param value     新的属性值
     */
    public static void setFieldValueByName(String fieldName, Object object, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Field getFieldValueByName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有属性
     *
     * @return 所有的属性【每一个属性添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        int len = fields.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Field field = fields[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(field.getModifiers());
            sb.append(modifier + " ");

            // 数据类型
            Class<?> type = field.getType();
            String typeName = type.getSimpleName();
            sb.append(typeName + " ");

            // 属性名
            String fieldName = field.getName();
            sb.append(fieldName + ";");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有公共的属性
     *
     * @return 所有公共的属性【每一个属性添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getPublicFields(Class<?> clazz) {
        Field[] fields = clazz.getFields();
        int len = fields.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Field field = fields[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(field.getModifiers());
            sb.append(modifier + " ");

            // 数据类型
            Class<?> type = field.getType();
            String typeName = type.getSimpleName();
            sb.append(typeName + " ");

            // 属性名
            String fieldName = field.getName();
            sb.append(fieldName + ";");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有构造方法
     *
     * @return 所有的构造方法【每一个构造方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        int len = constructors.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Constructor<?> constructor = constructors[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(constructor.getModifiers());
            sb.append(modifier + " ");

            // 方法名（类名）
            String constructorName = clazz.getSimpleName();
            sb.append(constructorName + " (");

            // 形参列表
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            int length = parameterTypes.length;
            for (int j = 0; j < length; j++) {
                Class<?> parameterType = parameterTypes[j];

                String parameterTypeName = parameterType.getSimpleName();

                if (j < length - 1) {
                    sb.append(parameterTypeName + ", ");
                } else {
                    sb.append(parameterTypeName);
                }

            }

            sb.append(") {}");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有包含注解的方法
     *
     * @param clazz      类
     * @param annotation 注解类
     * @return
     */
    public static List<Method> getMethodsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> list = new ArrayList<>();
        for (Method method : methods) {
            if (method.getAnnotation(annotation) != null) {
                list.add(method);
            }
        }
        return list;
    }

    /**
     * 获取所有自身的方法
     *
     * @return 所有自身的方法【每一个方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getMethods(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        int len = methods.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Method method = methods[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(method.getModifiers());
            sb.append(modifier + " ");

            // 返回值类型
            Class<?> returnClass = method.getReturnType();
            String returnType = returnClass.getSimpleName();
            sb.append(returnType + " ");

            // 方法名
            String methodName = method.getName();
            sb.append(methodName + " (");

            // 形参列表
            Class<?>[] parameterTypes = method.getParameterTypes();
            int length = parameterTypes.length;

            for (int j = 0; j < length; j++) {
                Class<?> parameterType = parameterTypes[j];

                // 形参类型
                String parameterTypeName = parameterType.getSimpleName();

                if (j < length - 1) {
                    sb.append(parameterTypeName + ", ");
                } else {
                    sb.append(parameterTypeName);
                }

            }

            sb.append(") {}");

            list.add(sb);
        }

        return list;
    }

    /**
     * 获取所有公共的方法
     *
     * @return 所有公共的方法【每一个方法添加到StringBuilder中，最后保存到一个List集合中】
     */
    public static List<StringBuilder> getPublicMethods(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        int len = methods.length;

        List<StringBuilder> list = new ArrayList<StringBuilder>();
        StringBuilder sb = null;
        for (int i = 0; i < len; i++) {
            Method method = methods[i];
            sb = new StringBuilder();

            // 修饰符
            String modifier = Modifier.toString(method.getModifiers());
            sb.append(modifier + " ");

            // 返回值类型
            Class<?> returnClass = method.getReturnType();
            String returnType = returnClass.getSimpleName();
            sb.append(returnType + " ");

            // 方法名
            String methodName = method.getName();
            sb.append(methodName + " (");

            // 形参列表
            Class<?>[] parameterTypes = method.getParameterTypes();
            int length = parameterTypes.length;

            for (int j = 0; j < length; j++) {
                Class<?> parameterType = parameterTypes[j];

                // 形参类型
                String parameterTypeName = parameterType.getSimpleName();

                if (j < length - 1) {
                    sb.append(parameterTypeName + ", ");
                } else {
                    sb.append(parameterTypeName);
                }

            }

            sb.append(") {}");

            list.add(sb);
        }

        return list;
    }
}
