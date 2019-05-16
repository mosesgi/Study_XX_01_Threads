package com.moses.study.future;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class UseFuture {
	class UseCallable implements Callable<Integer>{
		private int sum;

		@Override
		public Integer call() throws Exception {
			System.out.println("Callable子线程开始计算...");
			Thread.sleep(2000);
			for(int i = 0; i<5000; i++) {
				sum+=i;
			}
			System.out.println("Callable子线程完成计算. result: " + sum);
			return sum;
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		UseCallable use = new UseFuture().new UseCallable();
		
		FutureTask<Integer> ft = new FutureTask<>(use);
		new Thread(ft).start();
		
		Random r = new Random();
		Thread.sleep(1);
		if(r.nextBoolean()) {
			System.out.println("Get callable result = " + ft.get());
		} else {
			System.out.println("中断计算");
			ft.cancel(true);
		}
	}
}
