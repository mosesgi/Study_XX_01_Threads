package com.moses.study;

public class EndRunnable {
	class UseRunnable implements Runnable{

		@Override
		public void run() {
			String threadName = Thread.currentThread().getName();
			while(!Thread.currentThread().isInterrupted()) {
				System.out.println(threadName + " is running!");
			}
			System.out.println(threadName + " is interrupted.");
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		UseRunnable runnable = new EndRunnable().new UseRunnable();
		Thread t1 = new Thread(runnable);
		t1.start();
		Thread.sleep(20);
		t1.interrupt();
	}
}
