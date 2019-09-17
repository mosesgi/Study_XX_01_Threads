package com.moses.study.completionTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletionCase {
	private final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
	private final int TOTAL_TASK = Runtime.getRuntime().availableProcessors()*10;
	
	//自己写集合来实现获取线程池中任务的返回结果
	public void testByQueue() throws Exception{
		long start = System.currentTimeMillis();
		//所有任务休眠的总时长
		AtomicInteger count = new AtomicInteger(0);
		ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
		//容器存放提交给线程池的任务
		BlockingQueue<Future<Integer>> queue = new LinkedBlockingQueue<>();
		for(int i=0; i<TOTAL_TASK; i++) {
			Future<Integer> future = pool.submit(new WorkTask("ExecTask_" + i));
			queue.add(future);
		}
		
		for(int i=0; i<TOTAL_TASK; i++) {
			int sleptTime = queue.take().get();
			System.out.println(" slept " + sleptTime + " ms...");
			count.addAndGet(sleptTime);
		}
		
		pool.shutdown();
		System.out.println("--------tasks sleep time " + count.get() + " ms, and spent time " 
				+ (System.currentTimeMillis() - start) + " ms");
	}
	
	//通过CompletionService实现获取线程池中任务的返回结果
	public void testByCompletion() throws Exception{
		long start = System.currentTimeMillis();
		//所有任务休眠的总时长
		AtomicInteger count = new AtomicInteger(0);
		ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
		CompletionService<Integer> cService = new ExecutorCompletionService<>(pool);
		
		for(int i=0; i<TOTAL_TASK; i++) {
			cService.submit(new WorkTask("ExecTask_" + i));
		}
		
		for(int i=0; i<TOTAL_TASK; i++) {
			int sleptTime = cService.take().get();
			System.out.println(" slept " + sleptTime + " ms....");
			count.addAndGet(sleptTime);
		}
		
		pool.shutdown();
		System.out.println("-----------tasks sleep time " + count.get() + " ms, and spent time " 
				+ (System.currentTimeMillis() - start) + " ms");
	}
	
	public static void main(String[] args) throws Exception {
		CompletionCase cs = new CompletionCase();
		cs.testByQueue();
		
		cs.testByCompletion();
	}
}
