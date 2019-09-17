package com.moses.study.transferLockSample;

import com.moses.study.transferLockSample.service.ITransfer;
import com.moses.study.transferLockSample.service.TryLockTransferImpl;

/**
 * simulate Payment action for Payment company.
 * @author mosesji
 *
 */
public class PayAction {
	
	class TransferThread extends Thread{
		private String name;
		private UserAccount from;
		private UserAccount to;
		private int amount;
		private ITransfer transfer;
		
		public TransferThread(String name, UserAccount from, UserAccount to, int amount, ITransfer transfer) {
			super();
			this.name = name;
			this.from = from;
			this.to = to;
			this.amount = amount;
			this.transfer = transfer;
		}
		
		public void run() {
			Thread.currentThread().setName(name);
			try {
				transfer.transfer(from, to, amount);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		PayAction pa = new PayAction();
		UserAccount userA = new UserAccount("userA",20000);
		UserAccount userB = new UserAccount("userB",20000);
//		ITransfer transfer = new ProblemTransferImpl();		//dead lock. 动态顺序死锁
//		ITransfer transfer = new CompareAndLockTransferImpl();
		ITransfer transfer = new TryLockTransferImpl();
		TransferThread a2b = pa.new TransferThread("a2b", userA, userB, 2000, transfer);
		TransferThread b2a = pa.new TransferThread("b2a",userB, userA, 4000, transfer);
		
		a2b.start();
		b2a.start();
	}
}
