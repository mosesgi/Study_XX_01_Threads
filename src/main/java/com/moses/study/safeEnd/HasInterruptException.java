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
					//当InterruptedException抛出的时候，线程的中断状态将会被清除，重新置为false。
					//此时最好的一种做法是将线程中断状态重新置为true，这样才能最完全的保留线程当前的执行状况
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
