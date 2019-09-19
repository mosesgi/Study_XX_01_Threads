package com.moses.study.aqsAndLatchBarrier;

import com.moses.study.utils.SleepTools;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author Moses
 *
 */

public class LockDemo {
	private static Lock lock = new ReentrantLock();
	private int count;

	private AtomicInteger countAtomic = new AtomicInteger(0);

	public LockDemo(int count){
		this.count = count;
	}

	public void addCount(){
		count++;
	}

	public int getCount(){
		return count;
	}

	public void addCountAtomic(){
		countAtomic.incrementAndGet();
	}

	public int getCountAtomic(){
		return countAtomic.get();
	}


	static class AddThreadWithoutLock implements Runnable{
		private LockDemo lockDemo;

		public AddThreadWithoutLock(LockDemo lockDemo){
			this.lockDemo = lockDemo;
		}

		@Override
		public void run() {
			SleepTools.ms(10);
			for(int i=0; i<100; i++) {
				lockDemo.addCount();
			}
		}
	}

	static class AddThreadWithLock implements Runnable{
		private LockDemo lockDemo;

		public AddThreadWithLock(LockDemo lockDemo){
			this.lockDemo = lockDemo;
		}

		@Override
		public void run() {
			SleepTools.ms(10);
			for(int i=0; i<100; i++) {
//				reentrantLock();
				syncLock();
			}
		}

		private void reentrantLock(){
			lock.lock();
			try {
				lockDemo.addCount();
			} finally {
				lock.unlock();
			}
		}

		private void syncLock(){
			synchronized (lock){
				lockDemo.addCount();
			}
		}
	}

	static class AddThreadAtomic implements Runnable{
		private LockDemo lockDemo;

		public AddThreadAtomic(LockDemo lockDemo){
			this.lockDemo = lockDemo;
		}

		@Override
		public void run() {
			SleepTools.ms(10);
			for(int i=0; i<100; i++) {
				lockDemo.addCountAtomic();
			}
		}
	}
	
	public void multiThreadTest(){
		LockDemo lockDemo = new LockDemo(0);

		for(int i=0; i<100; i++){
			new Thread(new AddThreadWithoutLock(lockDemo)).start();
		}
		SleepTools.ms(100);
		System.out.println("WithoutLock result: " + lockDemo.getCount());

		lockDemo = new LockDemo(0);

		for(int i=0; i<100; i++) {
			new Thread(new AddThreadWithLock(lockDemo)).start();
		}
		SleepTools.ms(100);
		System.out.println("WithLock result: " + lockDemo.getCount());

		lockDemo = new LockDemo(0);
		for(int i=0; i<100; i++) {
			new Thread(new AddThreadAtomic(lockDemo)).start();
		}
		SleepTools.ms(100);
		System.out.println("WithAtomicInteger result: " + lockDemo.getCountAtomic());

	}
	
	public synchronized void incr2() {
		count++;
		incr2();
	}

	public synchronized void test3() {
		incr2();
	}


	public static void main(String[] args) {
		new LockDemo(0).multiThreadTest();
	}
	
}
