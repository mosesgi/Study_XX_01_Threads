package com.moses.study.semaphore;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class DBPoolSemaphore {
	private final static int POOL_SIZE = 10;
	private final Semaphore useful, useless;
	
	//存放数据库连接的容器
	private static LinkedList<Connection> pool = new LinkedList<Connection>();
	
	static {
		for(int i = 0; i<POOL_SIZE; i++) {
			pool.addLast(SqlConnectImpl.fetchConnection());
		}
	}
	
	public DBPoolSemaphore(){
		useful = new Semaphore(POOL_SIZE);
		useless = new Semaphore(0);
	}
	
	public Connection takeConnect() throws InterruptedException {
		useful.acquire();
		Connection con;
		synchronized(pool) { 
			con = pool.removeFirst();
		}
		useless.release();
		return con;
	}
	
	public void returnConnect(Connection con) throws InterruptedException{
		if(con == null) {
			return;
		}
		System.out.println("当前有 " + useful.getQueueLength() + " 个线程等待连接！！" + ", 可用连接数 " + useful.availablePermits());
		useless.acquire();
		synchronized(pool) {
			pool.addLast(con);
		}
		useful.release();
	}
	
}
