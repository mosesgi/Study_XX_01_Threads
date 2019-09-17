package com.moses.study.deadLock;

/**
 * normal dead lock scenario. 
 * Cause: Lock fetching sequence is different between threads.
 * @author mosesji
 *
 */
public class NormalDeadLock {
	private static Object valA = new Object();
	private static Object valB = new Object();
	
	static class ThreadA implements Runnable{
		public void run() {
			synchronized(valA) {
				System.out.println("ThreadA got lock ValA");
				synchronized(valB) {
					System.out.println("ThreadA got lock ValB");
				}
			}
		}
	}
	
	public static void methodB() {
		synchronized(valB) {
			System.out.println("MainThread got lock valB");
			synchronized(valA){
				System.out.println("MainThread got lock valA");
			}
		}
	}
	
	public static void main(String[] args) {
		new Thread(new ThreadA()).start();
		methodB();
	}
}
