package com.moses.study.completionTask;

import java.util.Random;
import java.util.concurrent.Callable;

import com.moses.study.utils.SleepTools;

public class WorkTask implements Callable<Integer>{
	private String name;
	public WorkTask(String name) {
		this.name = name;
	}
	
	@Override
	public Integer call() throws Exception {
		int sleepTime = new Random().nextInt(1000);
		
		SleepTools.ms(sleepTime);
		return sleepTime;
	}

}
