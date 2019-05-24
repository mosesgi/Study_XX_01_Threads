package com.moses.study.transferLockSample.service;

import com.moses.study.transferLockSample.UserAccount;

public class CompareAndLockTransferImpl implements ITransfer{
	private static Object tieLock = new Object(); //加时赛锁（相等时）

	@Override
	public void transfer(UserAccount from, UserAccount to, int amount) throws InterruptedException {
		int fromHash = System.identityHashCode(from);
		int toHash = System.identityHashCode(to);
		
		//lock small one first
		if(fromHash<toHash) {
			synchronized(from) {
				System.out.println(Thread.currentThread().getName() +" get "+from.getName());
				synchronized(to) {
					System.out.println(Thread.currentThread().getName() +" get "+to.getName());
					from.flyMoney(amount);
					to.addMoney(amount);
				}
			}
		} else if (fromHash>toHash) {
			synchronized(to) {
				System.out.println(Thread.currentThread().getName() +" get "+to.getName());
				synchronized(from) {
					System.out.println(Thread.currentThread().getName() +" get "+from.getName());
					from.flyMoney(amount);
					to.addMoney(amount);
				}
			}
		} else {
			synchronized(tieLock) {
				synchronized(from) {
					synchronized(to) {
						from.flyMoney(amount);
						to.addMoney(amount);
					}
				}
			}
		}
	}

}
