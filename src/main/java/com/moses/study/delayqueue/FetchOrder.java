package com.moses.study.delayqueue;

import java.util.concurrent.DelayQueue;

public class FetchOrder implements Runnable{
	private DelayQueue<ItemVo<Order>> queue;
	
	public FetchOrder(DelayQueue<ItemVo<Order>> queue) {
		this.queue = queue;
	}
	
	
	@Override
	public void run() {
		for(;;) {
			try {
				ItemVo<Order> item = queue.take();
				Order order = item.getData();
				System.out.println("get from queue: " + order.getOrderNo());
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
