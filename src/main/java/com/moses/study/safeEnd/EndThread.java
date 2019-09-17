package com.moses.study.safeEnd;

public class EndThread {
	private static class UseThread extends Thread{
		public UseThread(String name) {
			super(name);
		}
		
		public void run() {
			String threadName = Thread.currentThread().getName();
			while(!isInterrupted()) {		//如果是while(true), 即使被interrupt, 也不会停止
				System.out.println(threadName + " is Running!");
			}
			System.out.println(threadName + " interrupt flag is " + isInterrupted());
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread endThread = new UseThread("endThread");
		endThread.start();
		Thread.sleep(20);
		endThread.interrupt();
	}
}
