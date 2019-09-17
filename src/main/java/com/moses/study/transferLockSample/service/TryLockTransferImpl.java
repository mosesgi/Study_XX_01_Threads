package com.moses.study.transferLockSample.service;

import java.util.Random;

import com.moses.study.transferLockSample.UserAccount;
import com.moses.study.utils.SleepTools;

/**
 * use ReentrantLock.tryLock and sleep
 * @author mosesji
 *
 */
public class TryLockTransferImpl implements ITransfer{

	@Override
	public void transfer(UserAccount from, UserAccount to, int amount) throws InterruptedException {
		Random r = new Random();
		while(true) {
			if(from.getLock().tryLock()) {
				System.out.println(Thread.currentThread().getName() +" get "+from.getName());
				try {
					if(to.getLock().tryLock()) {
						try {
							System.out.println(Thread.currentThread().getName() +" get "+to.getName());
							from.flyMoney(amount);
							to.flyMoney(amount);
							break;
						} finally {
							to.getLock().unlock();
						}
					}
				} finally {
					from.getLock().unlock();
				}
			}
			SleepTools.ms(r.nextInt(10));		//不加随机睡眠，则容易产生活锁。两线程一直尝试取又取不到
		}
	}
	
}
