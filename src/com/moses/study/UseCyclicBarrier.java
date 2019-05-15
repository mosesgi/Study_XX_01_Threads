package com.moses.study;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

public class UseCyclicBarrier {
	CyclicBarrier cb = new CyclicBarrier(5);
	ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
	
	class SubThread extends Thread{
		
	}
}
