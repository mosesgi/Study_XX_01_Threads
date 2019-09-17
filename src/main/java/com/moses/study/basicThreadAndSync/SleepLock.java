package com.moses.study.basicThreadAndSync;

/**
 *@author 
 *
 *类说明：线程B需等待线程A的synchronized块执行完毕后才取得锁
 */
public class SleepLock {
    private Object lock = new Object();

    public static void main(String[] args) {
        SleepLock sleepTest = new SleepLock();
        Thread threadA = sleepTest.new ThreadSleep();
        threadA.setName("ThreadSleep");
        Thread threadB = sleepTest.new ThreadNotSleep();
        threadB.setName("ThreadNotSleep");
        threadA.start();
        try {
            Thread.sleep(1000);
            System.out.println(" Main slept! time=" +System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadB.start();
    }

    private class ThreadSleep extends Thread{

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName+" will take the lock. time="+System.currentTimeMillis());
            try {

                synchronized(lock) {
                    System.out.println(threadName+" taking the lock. time="+System.currentTimeMillis());
                    Thread.sleep(5000);
                    System.out.println("Finish the work: "+threadName + ". time=" + System.currentTimeMillis());
                }
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ThreadNotSleep extends Thread{

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName+" will take the lock time="+System.currentTimeMillis());
            synchronized(lock) {
                System.out.println(threadName+" taking the lock time="+System.currentTimeMillis());
                System.out.println("Finish the work: "+threadName);
            }
        }
    }
}
