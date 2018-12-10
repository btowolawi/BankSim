package edu.temple.cis.c3238.banksim;



public class Bank {

    public static final int NTEST = 10;
    private final Account[] accounts;
    private long ntransacts = 0;
    private final int initialBalance;
    private final int numAccounts;
    private int count = 0;// A count of how many transactions were made in one iteration
    private int noTransactionsMade = 0; // A count of how many times a transaction wasn't made
    private String sTransfers = ""; // string of transfers

    public Bank(int numAccounts, int initialBalance) {
        this.initialBalance = initialBalance;
        this.numAccounts = numAccounts;
        accounts = new Account[numAccounts];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(this, i, initialBalance);
        }
        ntransacts = 0;
    }

    public void transfer(int from, int to, int amount) {
        if (accounts[from].withdraw(amount)) {
            accounts[to].deposit(amount);
            count++;
            // Creates a String indicated how much was transfered, and between which accounts
            sT(String.format("*%d was transfered from Account[%d] to Account[%d]", amount, from, to));    
        }

        if (shouldTest()) test();
    }

    public void test() { 
        synchronized(Account.class)
        {
        	//If no transactions were made, it adds it to the noTransactionsMade tracker, so it can print how many times no 
        	//transactions were made
        	if(count == 0) {
        		noTransactionsMade++;
        	}
	        else {
	        	//Asterisks are just for a visual division
	        	System.out.println("\n********");
	        	System.out.println("\n(No transactions were made x " + noTransactionsMade + ")\n");
	        	System.out.println("********\n");
	        	noTransactionsMade = 0;
	            int sum = 0;
	            for (Account account : accounts) {
	                System.out.printf("%s %s%n", 
	                        Thread.currentThread().toString(), account.toString());
	                sum += account.getBalance();
	            }
	            //Prints the stings that dictate what transfers were made
	            System.out.println(sTransfers);
		        sTransfers = "";
	            System.out.println(Thread.currentThread().toString() + 
	                    " Sum: " + sum);
	            if (sum != numAccounts * initialBalance) {
	                System.out.println(Thread.currentThread().toString() + 
	                        " Money was gained or lost");
	                System.exit(1);
	            } else {
	                System.out.println(Thread.currentThread().toString() + 
	                        " The bank is in balance");
	            }
	        }
        	count = 0;
        }
    }

    public int size() {
        return accounts.length;
    }
    
    
    public synchronized boolean shouldTest() {
        return ++ntransacts % NTEST == 0;
    }
    
    //Creates a string of all the transactions, if any, that were made.
    //This method is not necessary but was created so that it does not ruin the visual pattern 
    public void sT(String s) {
    	if (sTransfers.equals("")) {
    		sTransfers += s;
    	}
    	else {
    		sTransfers = sTransfers + "\n" + s; 
    	}
    }

}