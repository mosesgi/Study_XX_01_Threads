package com.moses.study.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicArray {
	int[] value = new int[] {1, 2};
	
	AtomicIntegerArray ai = new AtomicIntegerArray(value);
	
	public static void main(String[] args) {
		AtomicArray aa = new AtomicArray();
		AtomicIntegerArray ai = aa.ai;
		ai.getAndSet(0, 3);
		System.out.println(ai.get(0));
		System.out.println(aa.value[0]);
	}
}
