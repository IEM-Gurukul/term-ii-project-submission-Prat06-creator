package model;
import exception.*;
import java.util.ArrayList;
import java.util.List;
public abstract class Account {
    protected int accountId;
    protected String holderName;
    protected double balance;
    protected List<Transaction> transactions = new ArrayList<>();
    public Account(int id, String name, double balance){
        this.accountId = id;
        this.holderName = name;
        this.balance = balance;
    }
    public int getAccountId(){
        return accountId;
    }
    public double getBalance(){
        return balance;
    }

    public List<Transaction> getTransactions(){
    return transactions;}

    public void deposit(double amount) throws InvalidAmountException {
        if(amount <= 0)
            throw new InvalidAmountException("Invalid deposit amount");

        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount));
    }

    public abstract void withdraw(double amount)
            throws InsufficientFundsException, InvalidAmountException;
}