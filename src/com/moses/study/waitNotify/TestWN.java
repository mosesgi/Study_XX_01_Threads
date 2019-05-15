package com.moses.study.waitNotify;

public class TestWN {
	private Express express = new Express(0, Express.BJ);
	
	private class CheckDistance extends Thread{
		public void run() {
			express.waitDistance();
		}
	}
	
	private class CheckLocation extends Thread{
		public void run() {
			express.waitLocation();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		TestWN test = new TestWN();
		Thread d1 = test.new CheckDistance();
		Thread d2 = test.new CheckDistance();
		Thread d3 = test.new CheckDistance();
		
		Thread l1 = test.new CheckLocation();
		Thread l2 = test.new CheckLocation();
		Thread l3 = test.new CheckLocation();
		
		d1.start();
		d2.start();
		d3.start();
		l1.start();
		l2.start();
		l3.start();
		
		Thread.sleep(1000);
		
		test.express.changeLocation("shanghai");
	}
}
