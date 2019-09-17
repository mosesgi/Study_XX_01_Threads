package com.moses.study.threadFramework.service;

import java.util.concurrent.DelayQueue;

import com.moses.study.threadFramework.entity.DelayQueueItem;

/**
 * Store processed Jobs in DelayQueue, remove expired task after expireTime.
 * @author Moses
 *
 */
public class ProcessedJobCleaner {
	//Delay queue to store the Job that has been processed and waiting to be removed.
	private static DelayQueue<DelayQueueItem<String>> delayQueue = new DelayQueue<DelayQueueItem<String>>();
	
	private ProcessedJobCleaner() {}
	private static class CleanerHolder{
		public static ProcessedJobCleaner jobCleaner = new ProcessedJobCleaner();
	}
	public static ProcessedJobCleaner getInstance() {
		return CleanerHolder.jobCleaner;
	}
	
	private static class CleanerThread implements Runnable{

		@Override
		public void run() {
			while(true) {
				try {
					DelayQueueItem<String> item = delayQueue.take();
					String jobName = item.getData();
					MultiTaskExecutor.getMap().remove(jobName);
					System.out.println(jobName + " is out of time, remove from Executor's Map!");
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static {
		Thread t = new Thread(new CleanerThread());
		t.setDaemon(true);
		t.start();
		System.out.println("ProcessedJobCleaner Daemon Thread started...");
	}
	
	public static void addProcessedJob(String jobName, long activeDurationTime) {
		DelayQueueItem<String> item = new DelayQueueItem<>(activeDurationTime, jobName);
		delayQueue.offer(item);
		System.out.println("Job[" + jobName + "] completed. Putting to expire queue, will expire in :" + activeDurationTime + " ms.");
	}
	
}
