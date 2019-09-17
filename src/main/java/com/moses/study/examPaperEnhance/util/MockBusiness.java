package com.moses.study.examPaperEnhance.util;

public class MockBusiness {
	public static void doBusiness(int sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
