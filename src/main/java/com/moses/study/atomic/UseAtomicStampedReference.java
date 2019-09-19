package com.moses.study.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

public class UseAtomicStampedReference {
	AtomicStampedReference<String> asr = new AtomicStampedReference<>("Mark", 0);
	
	public static void main(String[] args) throws InterruptedException {
		UseAtomicStampedReference uasr = new UseAtomicStampedReference();
		AtomicStampedReference<String> asr = uasr.asr;
		
		final int oldStamp = asr.getStamp();
		final String oldRef = asr.getReference();
		
		System.out.println(oldRef + "===============" + oldStamp);
		
		Thread rightStampThread = new Thread(() -> System.out.println(Thread.currentThread().getName() + "当前变量值: " + oldRef 
					+ "当前版本戳: " + oldStamp + "-" + asr.compareAndSet(oldRef, oldRef + "Java", oldStamp, oldStamp+1)));
		
		Thread errorStampThread = new Thread(() -> System.out.println(Thread.currentThread().getName() + "当前变量值: " + asr.getReference() 
					+ "当前版本戳: " + asr.getStamp() + "-" + asr.compareAndSet(asr.getReference(), asr.getReference() + "C", oldStamp, oldStamp + 1)));
		
		
		rightStampThread.start();
		rightStampThread.join();
		errorStampThread.start();
		errorStampThread.join();
		System.out.println(asr.getReference() + "================" + asr.getStamp());
	}
}
