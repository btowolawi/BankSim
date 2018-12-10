package edu.temple.cis.c3238.banksim;
import java.util.concurrent.*;


public class Account {

    private volatile int balance;
    private final int id;
    private final Bank myBank;
    private static Semaphore sem;

    public Account(Bank myBank, int id, int initialBalance) {
        this.myBank = myBank;
        this.id = id;
        balance = initialBalance;
        sem = new Semaphore(1);//1 available permit at a time
    }

    public int getBalance() {
        return balance;
    }

    
    public boolean withdraw(int amount) {
        synchronized(this){
            try{
            	if (amount <= balance) {
            		sem.acquire();
                    int currentBalance = balance;
                    int newBalance = currentBalance - amount;
                    balance = newBalance;
                    sem.release();
                    return true;
                    
                }
                else {
                    return false;
                }
            }
            catch(InterruptedException e) {
	            e.printStackTrace();
            }
            
        }
      //It only gets this far if an reaches the exception, and it that case, the withdraw wasn't successful
        return false;
    }

    public void deposit(int amount) {
        try{
            sem.acquire();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        try{
            int currentBalance = balance;
            int newBalance = currentBalance + amount;
            balance = newBalance;
        }
        finally{
            sem.release();
        }

    }
 
    
    @Override
    public String toString() {
        return String.format("Account[%d] balance %d", id, balance);
    }
}