package com.moses.study.schedulePool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.moses.study.utils.SleepTools;

public class ScheduleWorkerTime implements Runnable {
	public final static int Long_8 = 8;
	public final static int Short_2 = 2;
	public final static int Normal_5 = 5;
	
	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static AtomicInteger count = new AtomicInteger(0);
	
	@Override
	public void run() {
		if(count.get()==0) {
			System.out.println("Long_8...begin: " + formatter.format(new Date()));
			SleepTools.second(Long_8);
			System.out.println("Long_8...end: " + formatter.format(new Date()));
			count.incrementAndGet();
		} else if(count.get()==1) {
			System.out.println("Short_2...begin: " + formatter.format(new Date()));
			SleepTools.second(Short_2);
			System.out.println("Short_2...end: " + formatter.format(new Date()));
			count.incrementAndGet();
		} else {
			System.out.println("Normal_5..begin: " + formatter.format(new Date()));
			SleepTools.second(Normal_5);
			System.out.println("Normal_5...end: " + formatter.format(new Date()));
			count.incrementAndGet();
		}
	}
	
	public static void main(String[] args) {
		ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);
		
		//time interval 6 sec
		schedule.scheduleAtFixedRate(new ScheduleWorkerTime(), 0, 6000, TimeUnit.MILLISECONDS);
	}

}
