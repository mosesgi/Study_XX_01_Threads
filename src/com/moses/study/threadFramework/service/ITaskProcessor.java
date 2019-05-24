package com.moses.study.threadFramework.service;

import com.moses.study.threadFramework.entity.TaskResult;

public interface ITaskProcessor<T, R> {
	/**
	 * @param data the business object which will be used by executeTask
	 * @return Result after business logic is performed.
	 */
	TaskResult<R> executeTask(T data);
}
