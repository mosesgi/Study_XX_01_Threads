package com.moses.study.singleton;


/**
 * 懒汉式 - 内部类延迟初始化
 * @author mosesji
 *
 */
public class SingleLazyInit {
	private SingleLazyInit() {}
	
	private static class InstanceHolder{
		public static SingleLazyInit instance = new SingleLazyInit();
	}
	
	public static SingleLazyInit getInstance() {
		return InstanceHolder.instance;
	}
}
