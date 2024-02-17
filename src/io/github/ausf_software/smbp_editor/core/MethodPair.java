package io.github.ausf_software.smbp_editor.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Класс содержащий объект <code>MethodPair</code> и <code>Object</code>,
 * из которого вызывается этот метод.
 * @since 1.0
 * @version 1.0
 * @author Daniil Scherbina
 */
public class MethodPair {
    private Object obj;
    private Method method;

    /**
     * Создает экземпляр <code>MethodPair</code> по
     * <code>MethodPair</code> и <code>Object</code>
     * @param obj объект у которого необходимо вызвать метод
     * @param method метод который необходимо вызвать
     */
    public MethodPair(Object obj, Method method) {
        this.obj = obj;
        this.method = method;
    }

    /**
     * Вызывает метод с заданными аргументами.
     * @param args массив аргументов для вызова метода
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void invoke(Object[] args) throws InvocationTargetException, IllegalAccessException {
        method.invoke(obj, args);
    }

    /**
     * Возвращает объект <code>MethodPair</code>
     * @return объект <code>MethodPair</code>
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Возвращает объект <code>Object</code>
     * @return объект <code>Object</code>
     */
    public Object getObj() {
        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodPair that = (MethodPair) o;

        if (!obj.equals(that.obj)) return false;
        return method.equals(that.method);
    }

    @Override
    public int hashCode() {
        int result = obj.hashCode();
        result = 31 * result + method.hashCode();
        return result;
    }
}
