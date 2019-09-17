package com.moses.study.aqsAndLatchBarrier.rw;

import java.util.Random;

import com.moses.study.utils.SleepTools;

public class BusiApp {
	static final int readWriteRatio = 10;
	static final int minThreadCount = 3;
	
	class GetThread implements Runnable{
		private GoodsService goodsService;
		
		public GetThread(GoodsService goodsService) {
			this.goodsService = goodsService;
		}
		
		public void run() {
			long start = System.currentTimeMillis();
			for(int i=0;i<100;i++) {
				goodsService.getNum();
			}
			System.out.println(Thread.currentThread().getName()+"_read goodsInfo costs:" + (System.currentTimeMillis()-start) + "ms");
		}
	}
	
	class SetThread implements Runnable{
		private GoodsService goodsService;
		
		public SetThread(GoodsService goodsService) {
			this.goodsService = goodsService;
		}
		
		public void run() {
			long start = System.currentTimeMillis();
			Random r = new Random();
			for(int i=0;i<10;i++) {
				SleepTools.ms(50);
				goodsService.setNum(r.nextInt(10));
			}
			System.out.println(Thread.currentThread().getName()+"_set goodsInfo costs:" + (System.currentTimeMillis()-start) + "ms");
		}
	}
	
	public static void main(String[] args) {
		BusiApp busiApp = new BusiApp();
		GoodsInfo goodsInfo = new GoodsInfo("Cup", 100000, 10000);
//		GoodsService goodsService = new GoodsServiceSyncImpl(goodsInfo);
		GoodsService goodsService = new GoodsServiceRwLockImpl(goodsInfo);
		for(int i=0;i<minThreadCount; i++) {
			Thread setT = new Thread(busiApp.new SetThread(goodsService));
			for(int j=0; j<readWriteRatio; j++) {
				Thread getT = new Thread(busiApp.new GetThread(goodsService));
				getT.start();
			}
			SleepTools.ms(100);
			setT.start();
		}
	}
}
