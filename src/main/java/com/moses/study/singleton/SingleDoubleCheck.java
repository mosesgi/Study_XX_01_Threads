package com.moses.study.singleton;

public class SingleDoubleCheck {
	private volatile static SingleDoubleCheck singleInstance;	//不加volatile依然会有问题。必须加volatile才可保证
	
	private SingleDoubleCheck() {
		
	}
	
	public static SingleDoubleCheck getInstance() {
		if(singleInstance == null) {
			synchronized(SingleDoubleCheck.class) {
				if(singleInstance == null) {
					singleInstance = new SingleDoubleCheck();
				}
			}
		}
		return singleInstance;
	}
}
