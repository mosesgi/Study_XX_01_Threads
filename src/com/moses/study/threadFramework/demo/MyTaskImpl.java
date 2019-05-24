package com.moses.study.threadFramework.demo;

import java.util.Random;

import com.moses.study.threadFramework.entity.TaskResult;
import com.moses.study.threadFramework.entity.TaskResultType;
import com.moses.study.threadFramework.service.ITaskProcessor;
import com.moses.study.utils.SleepTools;

public class MyTaskImpl implements ITaskProcessor<Integer, Integer>{

	@Override
	public TaskResult<Integer> executeTask(Integer data) {
		Random r = new Random();
		int time = r.nextInt(500);
		SleepTools.ms(time);
		if(time < 400) {
			Integer returnValue = data.intValue() + time;
			return new TaskResult<Integer>(TaskResultType.Success, returnValue);
		} else if(time >=400 && time <450) {
			return new TaskResult<Integer>(TaskResultType.Failure, -1, "Failure");
		} else {
			throw new RuntimeException("Random Error occurs in job: " + time);
		}
	}

}
