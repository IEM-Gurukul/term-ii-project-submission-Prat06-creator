package service;
import java.util.List;
import repository.AccountRepository;
import model.*;
import exception.*;
public class BankingService {
    private final AccountRepository repository;
    private AccountRepository repo;

    public BankingService(AccountRepository repo){
        this.repository = repo;
    }

    public void createAccount(Account account){
        repository.save(account);
    }

    public void deposit(int id,double amount)
            throws Exception {

        Account acc = repository.findById(id);

        if(acc == null)
            throw new AccountNotFoundException("Account not found");

        acc.deposit(amount);
    }

    public void withdraw(int id,double amount)
            throws Exception {

        Account acc = repository.findById(id);

        if(acc == null)
            throw new AccountNotFoundException("Account not found");

        acc.withdraw(amount);
    }

    public void transfer(int from,int to,double amount)
            throws Exception {

        Account a = repository.findById(from);
        Account b = repository.findById(to);

        if(a==null || b==null)
            throw new AccountNotFoundException("Account not found");

        a.withdraw(amount);
        b.deposit(amount);
    }

    public double getBalance(int id)
            throws AccountNotFoundException{

        Account acc = repository.findById(id);

        if(acc == null)
            throw new AccountNotFoundException("Account not found");

        return acc.getBalance();
    }

	public List<Transaction> getTransactions(int accountId)
        throws AccountNotFoundException {

    Account acc = repo.findAccount(accountId);

    return acc.getTransactions();
    }
}
