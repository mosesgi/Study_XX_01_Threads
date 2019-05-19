package com.moses.study.aqs;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author Moses
 *
 */

public class LockDemo {
	private Lock lock = new ReentrantLock();
	private int count;
	
	public void increment() {
		lock.lock();
		try {
			count++;
		} finally {
			lock.unlock();
		}
	}
	
	public synchronized void incr2() {
		count++;
		incr2();
	}
	
	public synchronized void test3() {
		incr2();
	}
	
}
