package com.moses.study.semaphore;

import java.sql.Connection;
import java.util.Random;

public class SemaphoreTest {
	private DBPoolSemaphore dbPool = new DBPoolSemaphore();
	
	class SubThread extends Thread{
		public void run() {
			Random r = new Random();
			long start = System.currentTimeMillis();
			try {
				Connection con = dbPool.takeConnect();
				System.out.println("Thread_" + Thread.currentThread().getId()
						+ "_获取连接池耗时 "+ (System.currentTimeMillis() - start) + " ms");
				Thread.sleep(100+r.nextInt(100));
				System.out.println("完成查询，归还连接！");
				dbPool.returnConnect(con);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		SemaphoreTest test = new SemaphoreTest();
		for(int i = 0; i<50; i++) {
			Thread t = test.new SubThread();
			t.start();
		}
	}
}
