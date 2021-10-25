package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Resume resume = new Resume();
        Field field = resume.getClass().getDeclaredFields()[0];
        System.out.println(field.getName());
        field.setAccessible(true);
        System.out.println(field.get(resume));
        field.set(resume,"pep");
        System.out.println(resume);

        Method method = resume.getClass().getMethods()[1];
        Method method1 = resume.getClass().getMethod("toString",null);
        System.out.println(method.invoke(resume));


    }
}
