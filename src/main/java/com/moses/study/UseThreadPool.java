package com.moses.study;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.moses.study.utils.SleepTools;

public class UseThreadPool {
	class Worker implements Runnable{
		private String taskName;
		private Random r = new Random();
		
		public Worker(String taskName) {
			this.taskName = taskName;
		}
		
		public String getName() {
			return taskName;
		}
		
		public void run() {
			System.out.println(Thread.currentThread().getName() + " process the task: " + taskName);
			SleepTools.ms(r.nextInt(100)*5);
		}
	}
	
	class CallWorker implements Callable<String>{
		private String taskName;
        private Random r = new Random();

        public CallWorker(String taskName){
            this.taskName = taskName;
        }

        public String getName() {
            return taskName;
        }    	

		@Override
		public String call() throws Exception {
            System.out.println(Thread.currentThread().getName()
            		+" process the task : " + taskName);
            return Thread.currentThread().getName()+":"+r.nextInt(100)*5;
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService pool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), 
				new ThreadPoolExecutor.DiscardOldestPolicy());
		
//		ExecutorService pool = Executors.newCachedThreadPool();
		UseThreadPool utp = new UseThreadPool();
		for(int i=0; i<6; i++) {
			Worker worker = utp.new Worker("worker_" + i);
			pool.execute(worker);
		}
		
		for(int i=0; i<6; i++) {
			CallWorker cw = utp.new CallWorker("callWorker_" + i);
			Future<String> result = pool.submit(cw);
			System.out.println(result.get());
		}
		pool.shutdown();
	}
}
