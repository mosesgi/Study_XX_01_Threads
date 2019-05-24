package com.moses.study.singleton;

public class SinglePropertyLazyInit {
	private Integer value;
	private Object obj; 	// might be big & complex
	
	public SinglePropertyLazyInit(Integer value) {
		super();
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
	
	private static class PropertyHolder{
		public static Object objHolder = new Object();	//won't be loaded till getObj() method is called.
	}
	
	public Object getObj() {
		return PropertyHolder.objHolder;
	}
}
