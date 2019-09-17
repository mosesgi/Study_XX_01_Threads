package com.moses.study.aqsAndLatchBarrier;

import java.util.concurrent.CountDownLatch;

public class UseCountdownLatch {
	CountDownLatch latch = new CountDownLatch(5);
	
	class InitThread extends Thread{
		public void run() {
			System.out.println("Thread_" + Thread.currentThread().getName() + " ready to init");
			latch.countDown();
			for(int i=0; i<3; i++) {
				System.out.println("Thread_" + Thread.currentThread().getName() + " continue to do its work...");
			}
		}
	}
	
	class BizThread extends Thread{
		public void run() {
			System.out.println("BizThread_" + Thread.currentThread().getName() + " start business");
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("BizThread_" + Thread.currentThread().getName() + " end business");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		UseCountdownLatch countLat = new UseCountdownLatch();
		CountDownLatch latchInClass = countLat.latch;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread_" + Thread.currentThread().getName()+" starts step 1");
				latchInClass.countDown();
				System.out.println("begin step 2");
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread_" + Thread.currentThread().getName()+" starts step 2");
				latchInClass.countDown();
			}
			
		}).start();
		
		countLat.new BizThread().start();
		for(int i = 0; i<=3; i++) {
			Thread t = new Thread(countLat.new InitThread());
			t.start();
		}
		
		latchInClass.await();
		System.out.println("Main thread finishes work.");
	}
}
