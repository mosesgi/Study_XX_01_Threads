package com.moses.study.waitNotify;

public class Express {
	protected static final String BJ = "BeiJing";
	private int distance;
	private String location;
	
	public Express() {}
	public Express(int distance, String location) {
		this.distance = distance;
		this.location = location;
	}

	/**
	 * wait - notify 标准范式
	 * notify方
	 * 1. 获取对象的锁
	 * 2. 改变条件
	 * 3. notify/notifyAll 通知等待线程
	 * @param newLoc
	 */
	public synchronized void changeLocation(String newLoc) {
		location = newLoc;
		notifyAll();
	}
	
	public synchronized void changeDistance(int newDistance) {
		distance = newDistance;
		notifyAll();
	}

	/**
	 * wait - notify 标准范式
	 * wait方
	 * 1. 获取对象的锁
	 * 2. 循环里判断条件是否满足，不满足调用wait方法
	 * 3. 条件满足执行业务逻辑
	 */
	public synchronized void waitLocation() {
		while(BJ.equals(location)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Check location thread " + Thread.currentThread().getId() + " is being notified.");
		}
		System.out.println("This site is now " + location + ", I will call user.");
	}
	
	public synchronized void waitDistance() {
		while(distance<100) {
			try {
				wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Check distance thread " + Thread.currentThread().getId()+ " is being notified");
		}
		System.out.println("This distnace is now " + distance + ", I will write to DB.");
	}
}
