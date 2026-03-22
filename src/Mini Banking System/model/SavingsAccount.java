package model;

import exception.*;

public class SavingsAccount extends Account {

    public SavingsAccount(int id, String name, double balance){
        super(id, name, balance);
    }

    @Override
    public void withdraw(double amount)
            throws InsufficientFundsException, InvalidAmountException {

        if(amount <= 0)
            throw new InvalidAmountException("Invalid withdrawal amount");

        if(balance < amount)
            throw new InsufficientFundsException("Insufficient funds");

        balance -= amount;
        transactions.add(new Transaction("WITHDRAW", amount));
    }
}