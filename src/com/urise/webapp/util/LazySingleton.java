package com.urise.webapp.util;

public class LazySingleton {
    private static LazySingleton INSTANCE;

    private LazySingleton() {
    }
// Lazy singleton
//    public static synchronized LazySingleton getINSTANCE() {
//        if(INSTANCE == null){
//            INSTANCE = new LazySingleton();
//        }
//        return INSTANCE;
//    }
    //too long waiting time for taking a monitor

    //double-check
//    public static  LazySingleton getINSTANCE() {
//        if(INSTANCE == null){
//            synchronized (LazySingleton.class) {
//              if(INSTANCE == null){
//                INSTANCE = new LazySingleton();
//              }
//            }
//        }
//        return INSTANCE;
//    }

    //On-demand holder
   private static class LazySingletonHolder{
       private static final LazySingleton INSTANCE = new LazySingleton();

       public static  LazySingleton getINSTANCE() {
           return LazySingletonHolder.INSTANCE;

       }
   }
}
