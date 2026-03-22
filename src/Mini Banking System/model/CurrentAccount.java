package model;
import exception.*;
public class CurrentAccount extends Account {
    private double overdraftLimit = 5000;
    public CurrentAccount(int id, String name, double balance){
        super(id, name, balance);
    }
    @Override
    public void withdraw(double amount)
            throws InsufficientFundsException, InvalidAmountException {
        if(amount <= 0)
            throw new InvalidAmountException("Invalid withdrawal amount");
        if(balance + overdraftLimit < amount)
            throw new InsufficientFundsException("Overdraft limit exceeded");
        balance -= amount;
        transactions.add(new Transaction("WITHDRAW", amount));
    }
}
