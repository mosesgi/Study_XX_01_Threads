package com.moses.study.forkJoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Fork - Join
 * RecursiveAction 无返回值
 * RecursiveTask 有返回值
 */
public class FindDirsFiles extends RecursiveAction {
	
	private File path;
	
	public FindDirsFiles(File path) {
		this.path = path;
	}

	@Override
	protected void compute() {
		List<FindDirsFiles> subTasks = new ArrayList<>();
		File[] files = path.listFiles();
		if(files != null) {
			for(File file: files) {
				if(file.isDirectory()) {
					subTasks.add(new FindDirsFiles(file));
				} else {
					if(file.getAbsolutePath().endsWith("txt")){
						System.out.println(file.getAbsolutePath());
					}
				}
			}
			
			if(!subTasks.isEmpty()) {
				for(FindDirsFiles subTask : invokeAll(subTasks)) {
					subTask.join();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		File root = new File("/Users/mosesji/Documents");
		FindDirsFiles task = new FindDirsFiles(root);
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);
		
		System.out.println("Task is running...");
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int otherWork = 0;
		for(int i=0; i<100; i++) {
			otherWork+=i;
		}
		System.out.println("Main thread done sth. result: " + otherWork);
		task.join();
		
		System.out.println("task end.");
	}

}
