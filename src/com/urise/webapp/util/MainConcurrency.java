package com.urise.webapp.util;

import com.urise.webapp.model.ListSection;

import java.util.ArrayList;
import java.util.List;

public class MainConcurrency {
    private static final Object lock = new Object();
    private static int counter;

    public static void main(String[] args) throws InterruptedException {
        final MainConcurrency mainConcurrency = new MainConcurrency();
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    // inc(lock);
                    mainConcurrency.inc(lock);
                }

            });
            thread.start();
            list.add(thread);
        }

        for (Thread t :
                list) {
            t.join();
        }
        System.out.println(counter);
    }

    private synchronized void inc(Object lock) {
        counter++;
    }
}

//     private static synchronized void inc(){
//        counter++;
//    }
//
//        private static void inc(Object lock) {
//        int a = 1;
//        synchronized (lock) {
//            counter++;
//            wait();
//        }
//    }

//    private void inc() {
//        int a = 1;
//        synchronized (this) {
//            try {
//                counter++;
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
