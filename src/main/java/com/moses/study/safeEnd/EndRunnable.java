package com.moses.study.safeEnd;

public class EndRunnable {
	private static class UseRunnable implements Runnable{

		@Override
		public void run() {
			String threadName = Thread.currentThread().getName();
			while(!Thread.currentThread().isInterrupted()) {
				System.out.println(threadName + " is running!");
			}
			System.out.println(threadName + " interrupt flag is " + Thread.currentThread().isInterrupted());
		}
		
	}
	
	public static void main(String[] args) {
		Thread t = new Thread(new UseRunnable());
		t.start();
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.interrupt();

	}
}
