package com.moses.study.threadFramework.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import com.moses.study.threadFramework.service.ITaskProcessor;

public class JobInfo<R> {
	private final String jobName;	//identify unique task
	private final int taskNum;		//tasks number in this job
	private final ITaskProcessor<?,?> taskProcessor;
	private final AtomicInteger successCount;
	private final AtomicInteger processedCount;
	private final LinkedBlockingDeque<TaskResult<R>> taskResultQueue;	//Result queue, get result from head, put result to bottom.
	private final long expireTime;
	
	public JobInfo(String jobName, int taskNum, ITaskProcessor<?,?> taskProcessor, long expireTime) {
		super();
		this.jobName = jobName;
		this.taskNum = taskNum;
		this.taskProcessor = taskProcessor;
		this.successCount = new AtomicInteger(0);
		this.processedCount = new AtomicInteger(0);
		this.taskResultQueue = new LinkedBlockingDeque<TaskResult<R>>(taskNum);
		this.expireTime = expireTime;
	}
	
	public ITaskProcessor<?, ?> getTaskProcessor(){
		return taskProcessor;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public int getTaskNum() {
		return taskNum;
	}
	
	public int getSuccessCount() {
		return successCount.get();
	}
	
	public int getProcessedCount() {
		return processedCount.get();
	}
	
	public int getFailedCount() {
		return processedCount.get() - successCount.get();
	}
	
	public long getExpireTime() {
		return expireTime;
	}
	
	public String getProgress() {
		return "Success[" + successCount.get() + "]/Current[" + processedCount.get() + "] Total[" + taskNum + "]";
	}
	
	//Get newly finished tasks' result info after previous time this method was called.
	public List<TaskResult<R>> getUpdatedTaskResultList(){
		List<TaskResult<R>> taskList = new LinkedList<>();
		TaskResult<R> taskResult;
		//get result from BQ, get until there is no object
		while((taskResult=taskResultQueue.pollFirst()) != null) {
			taskList.add(taskResult);
		}
		return taskList;
	}
	
	public void updateTaskResult(TaskResult<R> result) {
		if(TaskResultType.Success.equals(result.getResultType())) {
			successCount.incrementAndGet();
		}
		taskResultQueue.offer(result);
		processedCount.incrementAndGet();
	}
}
