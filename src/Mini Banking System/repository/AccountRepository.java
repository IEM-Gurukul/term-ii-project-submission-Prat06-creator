package repository;
import exception.AccountNotFoundException;
import java.util.HashMap;
import java.util.Map;
import model.Account;

public class AccountRepository {

    private Map<Integer, Account> accounts = new HashMap<>();

    public void save(Account account){
        accounts.put(account.getAccountId(), account);
    }

    public Account findAccount(int id) throws AccountNotFoundException {

    Account acc = accounts.get(id);

    if(acc == null){
        throw new AccountNotFoundException("Account not found");
    }

    return acc;
}

    public Account findById(int id){
        return accounts.get(id);
    }

    public boolean exists(int id){
        return accounts.containsKey(id);
    }
}