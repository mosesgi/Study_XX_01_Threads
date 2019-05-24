package com.moses.study.delayqueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ItemVo<T> implements Delayed {
	
	private long activeTime;		//到期时间，毫秒
	private T data;
	
	public ItemVo(long activeTime, T data) {
		super();
		this.data = data;
		this.activeTime = TimeUnit.NANOSECONDS.convert(activeTime, TimeUnit.MILLISECONDS) + System.nanoTime();
	}
	
	public long getActiveTime() {
		return activeTime;
	}

	public T getData() {
		return data;
	}

	@Override
	public int compareTo(Delayed o) {
		long d = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
		return d==0?0:(d>0?1:-1);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long d = unit.convert(this.activeTime-System.nanoTime(), TimeUnit.NANOSECONDS);
		return d;
	}
	
}
