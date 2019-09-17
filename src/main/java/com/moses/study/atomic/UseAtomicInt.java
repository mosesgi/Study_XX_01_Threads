package com.moses.study.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class UseAtomicInt {
	AtomicInteger ai = new AtomicInteger(10);
	
	public static void main(String[] args) {
		UseAtomicInt uai = new UseAtomicInt();
		AtomicInteger ai = uai.ai;
		System.out.println(ai.getAndIncrement());
		System.out.println(ai.incrementAndGet());
	}
}
