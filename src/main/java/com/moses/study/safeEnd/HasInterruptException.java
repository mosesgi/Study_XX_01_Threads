package com.moses.study.safeEnd;

public class HasInterruptException {
	private static class UseThread extends Thread{
		public UseThread(String name) {
			super(name);
		}
		
		public void run() {
			String name = Thread.currentThread().getName();
			while(!isInterrupted()) {
				try {
					Thread.sleep(100);
				} catch(InterruptedException e) {
					System.out.println(name + " interrupt flag is " + isInterrupted());
					interrupt();
					e.printStackTrace();
				}
				System.out.println(name);
			}
			System.out.println(name + " interrupt flag is " + isInterrupted());
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		UseThread endThread = new UseThread("HasInterruptEx");
		endThread.start();
		Thread.sleep(500);
		endThread.interrupt();
	}

}
