package com.moses.study.aqsAndLatchBarrier.rw;

import com.moses.study.utils.SleepTools;

public class GoodsServiceSyncImpl implements GoodsService{
	private GoodsInfo goodsInfo;
	
	public GoodsServiceSyncImpl(GoodsInfo goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
	
	@Override
	public synchronized GoodsInfo getNum() {
		SleepTools.ms(5);
		return this.goodsInfo;
	}

	@Override
	public synchronized void setNum(int number) {
		SleepTools.ms(5);
		this.goodsInfo.changeNumber(number);
	}
	
}
