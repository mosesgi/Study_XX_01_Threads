package com.moses.study.aqsAndLatchBarrier;

import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

public class UseCyclicBarrier {
	CyclicBarrier cb = new CyclicBarrier(5, new CollectResult());
	ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
	
	class SubThread extends Thread{
		public void run() {
			Long id = Thread.currentThread().getId();
			map.put(id+"", id);
			Random random = new Random();
			try {
				if(random.nextBoolean()) {
					Thread.sleep(2000);
					System.out.println("Thread " + id + " is doing something...");
				}
				System.out.println(id + " is awaiting");
				cb.await();
				Thread.sleep(1000);
				System.out.println(id + " doing business.");
			} catch(InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	class CollectResult extends Thread{
		public void run() {
			StringBuffer sb = new StringBuffer();
			for(Entry<String, Long> entry: map.entrySet()) {
				sb.append("[" + entry.getValue()+"]");
			}
			System.out.println("result is " + sb);
			System.out.println("CollectResult do other business...");
		}
	}
	
	public static void main(String[] args) {
		UseCyclicBarrier ub = new UseCyclicBarrier();
		for(int i = 0; i<=4; i++) {
			SubThread st = ub.new SubThread();
			st.start();
		}
	}
}
