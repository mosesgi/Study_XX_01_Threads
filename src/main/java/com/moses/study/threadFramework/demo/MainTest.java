package com.moses.study.threadFramework.demo;

import java.util.List;
import java.util.Random;

import com.moses.study.threadFramework.entity.TaskResult;
import com.moses.study.threadFramework.service.MultiTaskExecutor;
import com.moses.study.utils.SleepTools;

public class MainTest {
	private final static String JOB_NAME = "Calculate Number";
	private final static int TASK_SIZE = 1000;
	
	class QueryResult implements Runnable{
		private MultiTaskExecutor executor;
		
		public QueryResult(MultiTaskExecutor executor) {
			this.executor = executor;
		}
		
		public void run() {
			int i = 0;
			while(i<350) {
				List<TaskResult<String>> taskDetail = executor.getUpdatedTaskResultList(JOB_NAME);
				if(!taskDetail.isEmpty()) {
					System.out.println(executor.getTaskProgress(JOB_NAME));
					System.out.println(taskDetail);
				}
				SleepTools.ms(100);
				i++;
			}
		}
	}
	
	public static void main(String[] args) {
		MyTaskImpl myTaskImpl = new MyTaskImpl();
		MultiTaskExecutor executor = MultiTaskExecutor.getInstance();
		
		executor.registerJob(JOB_NAME, TASK_SIZE, myTaskImpl, 1000*5);
		Random r = new Random();
		for(int i = 0; i<TASK_SIZE; i++) {
			executor.putTask(JOB_NAME, r.nextInt(1000));
		}
		MainTest mt = new MainTest();
		Thread t = new Thread(mt.new QueryResult(executor));
		t.start();
	}
}
