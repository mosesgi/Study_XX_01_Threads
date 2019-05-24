package com.moses.study.mypool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MyThreadPool {
	//default Thread size
	private static int WORK_NUM = 5;
	//default task count in the queue
	private static int QUEUE_TASK_NUM = 100;
	
	//Thread array
	private WorkThread[] workThreads;
	
	// task queue as buffer
	private final BlockingQueue<Runnable> taskQueue;
	private final int worker_num;	//work number defined by user.
	
	public MyThreadPool() {
		this(WORK_NUM, QUEUE_TASK_NUM);
	}
	
	public MyThreadPool(int workerNum, int queueTaskNum) {
		if(workerNum <=0) workerNum = WORK_NUM;
		if(queueTaskNum<=0) queueTaskNum = QUEUE_TASK_NUM;
		this.worker_num = workerNum;
		taskQueue = new ArrayBlockingQueue<>(queueTaskNum);
		workThreads = new WorkThread[worker_num];
		for(int i=0; i<worker_num; i++) {
			workThreads[i] = new WorkThread();
			workThreads[i].start();
		}
//		Runtime.getRuntime().availableProcessors();
	}
	
	public void execute(Runnable task) {
		try {
			taskQueue.put(task);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void destroy(){
		System.out.println("Ready to close pool...");
		for(int i=0; i<worker_num; i++) {
			workThreads[i].stopWorker();
			workThreads[i] = null;  //help gc.
		}
		taskQueue.clear();
	}
	
	public String toString() {
		return "WorkThread number: " + worker_num + " wait task number: " + taskQueue.size();
	}
	
	private class WorkThread extends Thread{
		
		public void run() {
			Runnable r = null;
			try {
				while(!isInterrupted()) {
					r = taskQueue.take();
					if(r!= null) {
						System.out.println(getId() + " ready exec : " + r);
						r.run();
					}
					r = null; 	//help gc.
				}
			} catch(Exception e) {
				
			}
		}
		
		public void stopWorker() {
			interrupt();
		}
	}
}
