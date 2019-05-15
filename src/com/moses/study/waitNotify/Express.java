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
	
	public synchronized void changeLocation(String newLoc) {
		location = newLoc;
		notifyAll();
	}
	
	public synchronized void changeDistance(int newDistance) {
		distance = newDistance;
		notifyAll();
	}
	
	public synchronized void waitLocation() {
		while(BJ.equals(location)) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
