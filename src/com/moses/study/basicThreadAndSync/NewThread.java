package com.moses.study.basicThreadAndSync;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class NewThread {
	
	class UseRunnable implements Runnable{

		@Override
		public void run() {
			System.out.println("I implement Runnable!");
		}
		
	}
	
	class UseCallable implements Callable<String>{

		@Override
		public String call() throws Exception {
			System.out.println("I implement Callable!");
			return "Return result from Callable";
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		UseRunnable runnable = new NewThread().new UseRunnable();
		Thread t1 = new Thread(runnable);
		t1.start();
		
		UseCallable useCall = new NewThread().new UseCallable();
		FutureTask<String> ft = new FutureTask<>(useCall);
		new Thread(ft).start();
		
		System.out.println(ft.get());
	}
}
