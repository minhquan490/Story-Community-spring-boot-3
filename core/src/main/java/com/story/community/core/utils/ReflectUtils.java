package com.story.community.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

/**
 * Use this class if you want to play with all component of class
 * 
 * @author hoangquan
 */
public final class ReflectUtils {
    private ReflectUtils() {
        throw new UnsupportedOperationException("Can't instance ReflectUtils");
    }

    /**
     * Find annotation mark on the field
     * 
     * @param <T>        annotaion type
     * @param field      field use for find annotaion mark on it
     * @param annotation use for find on field
     * @return annotation or null if field wasn't mark by this annotation
     */
    public static <T extends Annotation> T getAnnotationOfField(Field field, Class<T> annotation) {
        return field.getAnnotation(annotation);
    }

    /**
     * Find field of class with given name
     * 
     * @param clazz     to find field on it
     * @param fieldName name of field
     * @return the field or null if field isn't exist
     */
    public static Field getFieldOfClass(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException e) {
            return null;
        }
    }

    /**
     * Find field of class with given annotation
     * 
     * @param <T>        annotation type
     * @param clazz      class to find field
     * @param annotation mark on it
     * @throws IllegalStateException if find greater than one element
     * @return the field or null if field isn't exist,
     */
    public static <T extends Annotation> Field getFieldOfClass(Class<?> clazz, Class<T> annotation) {
        ArrayList<Field> fields = (ArrayList<Field>) getFieldsOfClass(clazz, annotation);
        Assert.state(fields.size() == 1, "Find greater 1 element, use other method to find multi element");
        return fields.isEmpty() ? null : fields.get(0);
    }

    /**
     * Find all fields were marked by given annotation
     * 
     * @param <T>        type of annotation
     * @param clazz      class to find fields
     * @param annotation mark on field
     * @return collection of field or empty collection if no field mark by given
     *         annotation
     */
    public static <T extends Annotation> Collection<Field> getFieldsOfClass(Class<?> clazz, Class<T> annotation) {
        Collection<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * Find method of class with given method name
     * 
     * @param clazz      class to find method in it
     * @param methodName name of method to find
     * @return method result or null if method isn't existed in class
     */
    public static Method getMethodOfClass(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    public static Method getGetter(Class<?> clazz, String fieldName) {
        StringBuilder getterName = new StringBuilder("get");
        getterName.append(fieldName.substring(0, 1).toUpperCase());
        getterName.append(fieldName.substring(1));
        return getMethodOfClass(clazz, getterName.toString());
    }

    public static Method getSetter(Class<?> clazz, String fieldName) {
        StringBuilder getterName = new StringBuilder("set");
        getterName.append(fieldName.substring(0, 1).toUpperCase());
        getterName.append(fieldName.substring(1));
        return getMethodOfClass(clazz, getterName.toString());
    }

    public static Field getIdField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        return null;
    }

    public static <T extends Annotation> T getAnnotationOfMethod(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> extractClassFromGeneric(Class<?> clazz) {
        return (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
