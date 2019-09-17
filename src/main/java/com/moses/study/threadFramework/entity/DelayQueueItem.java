package com.moses.study.threadFramework.entity;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueItem<S> implements Delayed {
	private long expireNanoSeconds;		//Store exact expire nano seconds
	private S data;
	
	/**
	 * 
	 * @param activeDurationTime Pass active duration in Milliseconds.
	 * @param data
	 */
	public DelayQueueItem(long activeDurationTime, S data) {
		super();
		this.expireNanoSeconds = TimeUnit.NANOSECONDS.convert(activeDurationTime, TimeUnit.MILLISECONDS) + System.nanoTime();
		this.data = data;
	}
	
	public long getExpireNanoSeconds() {
		return expireNanoSeconds;
	}
	
	public S getData() {
		return data;
	}

	@Override
	public int compareTo(Delayed o) {
		long d = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
		return d==0?0:(d>0?1:-1);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		//return remaining delay in the given TimeUnit
		return unit.convert((this.expireNanoSeconds - System.nanoTime()), TimeUnit.NANOSECONDS);
	}
	
	
}
