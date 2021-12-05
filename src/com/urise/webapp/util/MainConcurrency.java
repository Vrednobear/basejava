package com.urise.webapp.util;

import com.urise.webapp.model.ListSection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MainConcurrency {
    private static final Object lock = new Object();
    private static int counter;
    private static final int THREADS_NUMBER= 10000;

    public static void main(String[] args) throws InterruptedException {
        final MainConcurrency mainConcurrency = new MainConcurrency();
        CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
    //    List<Thread> list = new ArrayList<>();
        ExecutorService service = Executors.newCachedThreadPool();
        ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(Executors.newSingleThreadExecutor());

        for (int i = 0; i < THREADS_NUMBER; i++) {
            service.submit(() ->{
          //  Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    // inc(lock);
                    mainConcurrency.inc();
                }
                latch.countDown();
            });
    //        thread.start();
    //        list.add(thread);
        }
        service.shutdown();
//        for (Thread t :
//                list) {
//            t.join();
//        }
        latch.await();
        System.out.println(counter);
    }

    private synchronized void inc() {
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
