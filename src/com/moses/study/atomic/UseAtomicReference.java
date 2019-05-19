package com.moses.study.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class UseAtomicReference {
	final AtomicReference<UserInfo> userRef = new AtomicReference<>();
	
	class UserInfo {
		private String name;
		private int age;
		
		public UserInfo(String name, int age) {
			this.name = name;
			this.age = age;
		}
		
		public String getName() {
			return name;
		}
		
		public int getAge() {
			return age;
		}
	}
	
	public static void main(String[] args) {
		UseAtomicReference uar = new UseAtomicReference();
		UserInfo user = uar.new UserInfo("Mark", 15);
		AtomicReference<UserInfo> userRef = uar.userRef;
		
		userRef.set(user);
		UserInfo updateUser = uar.new UserInfo("Bill", 17);
		userRef.compareAndSet(user, updateUser);
		System.out.println(userRef.get().getName());
		System.out.println(userRef.get().getAge());
		System.out.println(user.getName());
		System.out.println(user.getAge());
	}
}
