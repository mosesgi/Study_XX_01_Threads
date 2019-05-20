package com.moses.study.aqs.custom;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.moses.study.utils.SleepTools;

public class TestMyCustomLock {
	public void test() {
		final Lock lock = new ReentrantLock();
		final CountDownLatch latch = new CountDownLatch(10);
		
		class Worker extends Thread{
			CountDownLatch wLatch;
			public Worker(CountDownLatch latch) {
				this.wLatch = latch;
			}
			
			public void run() {
				lock.lock();
				try {
					SleepTools.second(1);
					System.out.println(Thread.currentThread().getName());
					SleepTools.second(1);
					wLatch.countDown();
				} finally {
					lock.unlock();
				}
				SleepTools.second(2);
			}
		}
		
		for(int i=0; i<10; i++) {
			Worker w = new Worker(latch);
			w.start();
		}
		
		for(int i=0; i<30; i++) {
			SleepTools.second(1);
			System.out.println();
		}
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		TestMyCustomLock tcl = new TestMyCustomLock();
		tcl.test();
		
	}
}
