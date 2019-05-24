package com.moses.study.threadFramework.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.moses.study.threadFramework.entity.JobInfo;
import com.moses.study.threadFramework.entity.TaskResult;
import com.moses.study.threadFramework.entity.TaskResultType;

public class MultiTaskExecutor {
	private static final int THREAD_COUNTS = Runtime.getRuntime().availableProcessors();
	//tasks queue
	private static BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(5000);
	//Thread pool, fixed size, limited queue
	private static ExecutorService threadPool = new ThreadPoolExecutor(THREAD_COUNTS, THREAD_COUNTS, 
				60, TimeUnit.SECONDS, taskQueue);
	//Map to hold all JobInfo entities
	private static ConcurrentHashMap<String, JobInfo<?>> jobInfoMap = new ConcurrentHashMap<>();
	
	
	public static Map<String, JobInfo<?>> getMap(){
		return jobInfoMap;
	}
	
	//Singleton
	private MultiTaskExecutor() {}
	private static class TaskExecutorHolder{
		public static MultiTaskExecutor executor = new MultiTaskExecutor();
	}
	
	public static MultiTaskExecutor getInstance() {
		return TaskExecutorHolder.executor;
	}
	
	//inner class to wrap tasks as Runnable class
	private static class RunnableTask<T, R> implements Runnable{
		private JobInfo<R> jobInfo;
		private T processData;
		
		public RunnableTask(JobInfo<R> jobInfo, T processData) {
			super();
			this.processData = processData;
			this.jobInfo = jobInfo;
		}
		
		public void run() {
			R r = null;
			ITaskProcessor<T, R> taskProcessor = (ITaskProcessor<T, R>)jobInfo.getTaskProcessor();
			TaskResult<R> result = null;
			
			try {
				//trigger the task using Dev defined method/logic
				result = taskProcessor.executeTask(processData);
				if(result == null) {
					result = new TaskResult<R>(TaskResultType.Exception, r, "result is null");
				}
				if(result.getResultType() == null) {
					if(result.getReason() == null) {
						result = new TaskResult<R>(TaskResultType.Exception, r, "reason is null");
					} else {
						result = new TaskResult<R>(TaskResultType.Exception, r, "result type is null, but reason is: " + result.getReason());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = new TaskResult<R>(TaskResultType.Exception, r, e.getMessage());
			} finally {
				jobInfo.updateTaskResult(result);
				if(jobInfo.getProcessedCount() == jobInfo.getTaskNum()) {
					ProcessedJobCleaner.addProcessedJob(jobInfo.getJobName(), jobInfo.getExpireTime());
				}
			}
		}
	}
	
	
	//Get JobInfo by jobName
	private <R> JobInfo<R> getJob(String jobName){
		JobInfo<R> jobInfo = (JobInfo<R>)jobInfoMap.get(jobName);
		if(jobInfo == null) {
			throw new RuntimeException(jobName + " does not exist!");
		}
		return jobInfo;
	}
	
	//add new Task to a job which is WIP.
	public <T,R> void putTask(String jobName, T t) {
		JobInfo<R> jobInfo = getJob(jobName);
		RunnableTask<T, R> task = new RunnableTask<T, R>(jobInfo, t);
		threadPool.execute(task);
	}
	
	//register new job with jobName, ITaskProcessor implementation, task num, etc.
	public <R> void registerJob(String jobName, int taskSize, ITaskProcessor<?, ?> taskProcessor, long expireTime) {
		JobInfo<R> jobInfo = new JobInfo(jobName, taskSize, taskProcessor, expireTime);
		if(jobInfoMap.putIfAbsent(jobName, jobInfo) != null) {
			throw new RuntimeException(jobName +" has been registered already!!");
		}
	}
	
	public <R> List<TaskResult<R>> getUpdatedTaskResultList(String jobName){
		JobInfo<R> jobInfo = getJob(jobName);
		return jobInfo.getUpdatedTaskResultList();
	}
	
	public <R> String getTaskProgress(String jobName) {
		JobInfo<R> jobInfo = getJob(jobName);
		return jobInfo.getProgress();
	}
}
