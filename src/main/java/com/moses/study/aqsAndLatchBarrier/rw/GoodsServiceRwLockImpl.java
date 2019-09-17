package com.moses.study.aqsAndLatchBarrier.rw;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.moses.study.utils.SleepTools;

public class GoodsServiceRwLockImpl implements GoodsService {
	private GoodsInfo goodsInfo;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock getLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();
	
	public GoodsServiceRwLockImpl(GoodsInfo goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	@Override
	public GoodsInfo getNum() {
		getLock.lock();
		try {
			SleepTools.ms(5);
			return goodsInfo;
		} finally {
			getLock.unlock();
		}
	}

	@Override
	public void setNum(int number) {
		writeLock.lock();
		try {
			SleepTools.ms(5);
			goodsInfo.changeNumber(number);
		} finally {
			writeLock.unlock();
		}
	}

}
