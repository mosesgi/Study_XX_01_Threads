package com.moses.study.threadFramework.entity;

public class TaskResult<R> {
	private final TaskResultType resultType;
	private final R returnData;		//returned result data
	private final String reason;		//failure reason
	
	public TaskResult(TaskResultType resultType, R returnData, String reason) {
		super();
		this.resultType = resultType;
		this.returnData = returnData;
		this.reason = reason;
	}
	
	public TaskResult(TaskResultType resultType, R returnData) {
		super();
		this.resultType = resultType;
		this.returnData = returnData;
		this.reason = "Success";
	}

	public TaskResultType getResultType() {
		return resultType;
	}

	public R getReturnData() {
		return returnData;
	}

	public String getReason() {
		return reason;
	}

	@Override
	public String toString() {
		return "TaskResult [resultType=" + resultType + ", returnData=" + returnData + ", reason=" + reason + "]";
	}
	
}
