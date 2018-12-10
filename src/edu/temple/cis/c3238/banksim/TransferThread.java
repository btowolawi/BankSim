package edu.temple.cis.c3238.banksim;


class TransferThread extends Thread {

    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;

    public TransferThread(Bank b, int from, int max) {
        bank = b;
        fromAccount = from;
        maxAmount = max;
    }

    @Override
    public void run() {
    	System.out.println("");
        for (int i = 0; i < 10000; i++) {
            int toAccount = (int) (bank.size() * Math.random());
            int amount = (int) (maxAmount * Math.random());
            
            synchronized(Account.class)
            {
                bank.transfer(fromAccount, toAccount, amount);
            }
            //If the thread has terminated, indicate which account terminated, and exit program
            if(isTerminated()) {
            	System.out.println("Account [" + i + "] has terminated");
            	System.out.println("Bank is close.");
            	System.exit(1);
            }
        }
    }
    
    //Checks to see in the current thread has terminated
    public boolean isTerminated() {
    	if(Thread.currentThread().getState() == Thread.State.TERMINATED) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
}