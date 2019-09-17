package com.moses.study.atomic;

public class VolatileUnsafe {
	class VolatileVar implements Runnable{
		private volatile int a = 0;

		@Override
		public void run() {
			String threadName = Thread.currentThread().getName();
			a = a++;
			System.out.println(threadName + "==============" + a);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			a = a+1;
			System.out.println(threadName + "==============" + a);
		}
		
	}
	
	public static void main(String[] args) {
		VolatileVar vv = new VolatileUnsafe().new VolatileVar();
		Thread t1 = new Thread(vv);
		Thread t2 = new Thread(vv);
		Thread t3 = new Thread(vv);
		Thread t4 = new Thread(vv);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
}
