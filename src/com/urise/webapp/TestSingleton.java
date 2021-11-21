package com.urise.webapp;

import com.urise.webapp.model.SectionType;

public class TestSingleton {
    private static TestSingleton instance;

    private TestSingleton() {

    }

    public static TestSingleton getInstance() {
        if (instance == null) {
            instance = new TestSingleton();
        }
        return instance;
    }

    public static void main(String[] args) {
       Singleton s = Singleton.valueOf("INSTANCE");
        System.out.println(s.name());
        System.out.println(s.ordinal());

        for (SectionType type :
                SectionType.values()) {
            System.out.println(type.getTitle());
        }
    }

    public enum Singleton{
        INSTANCE
    }
}
    
    
   