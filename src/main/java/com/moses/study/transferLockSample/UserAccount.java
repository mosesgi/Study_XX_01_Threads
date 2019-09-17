package com.moses.study.transferLockSample;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserAccount {
    //private int id;
    private final String name;//账户名称
    private int money;//账户余额

    //显式锁
    private final Lock lock = new ReentrantLock();

    public Lock getLock() {
        return lock;
    }

    public UserAccount(String name, int amount) {
        this.name = name;
        this.money = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return money;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "name='" + name + '\'' +
                ", money=" + money +
                '}';
    }

    //转入资金
    public void addMoney(int amount){
        money = money + amount;
    }

    //转出资金
    public void flyMoney(int amount){
        money = money - amount;
    }
}
