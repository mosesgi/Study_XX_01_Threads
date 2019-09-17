package com.moses.study.mypool;

import java.util.Random;

public class TestMyThreadPool {
	class MyTask implements Runnable{
		private String name;
		private Random r = new Random();
		
		public MyTask(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public void run() {
			try {
				Thread.sleep(r.nextInt(1000) + 2000);
				
			} catch(InterruptedException e) {
				System.out.println(Thread.currentThread().getId()+" sleep InterruptedException:"
                        +Thread.currentThread().isInterrupted());
			}
			System.out.println("Task " + name + " Completed.");
		}
		
		public String toString() {
			return "Thread MyTask - " + getName();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		TestMyThreadPool ttp = new TestMyThreadPool();
		MyThreadPool t = new MyThreadPool(3,0);
		
		t.execute(ttp.new MyTask("testA"));
		t.execute(ttp.new MyTask("testB"));
		t.execute(ttp.new MyTask("testC"));
		t.execute(ttp.new MyTask("testD"));
		t.execute(ttp.new MyTask("testE"));
		t.execute(ttp.new MyTask("testF"));
		
		System.out.println(t);
		Thread.sleep(10000);
		t.destroy();
		System.out.println(t);
	}
}
