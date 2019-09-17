package com.moses.study.transferLockSample.service;

import com.moses.study.transferLockSample.UserAccount;

public class ProblemTransferImpl implements ITransfer{

	@Override
	public void transfer(UserAccount from, UserAccount to, int amount) throws InterruptedException {
		synchronized(from){
			System.out.println(Thread.currentThread().getName() + " get " + from.getName());
			Thread.sleep(100);
			synchronized(to) {
				System.out.println(Thread.currentThread().getName() + " get " + to.getName());
				from.flyMoney(amount);
				to.addMoney(amount);
			}
		}
	}

}
