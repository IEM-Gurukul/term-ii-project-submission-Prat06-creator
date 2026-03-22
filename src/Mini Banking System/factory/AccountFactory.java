package factory;

import model.*;

public class AccountFactory {

    public static Account createAccount(String type,int id,String name,double balance){

        if(type.equalsIgnoreCase("SAVINGS"))
            return new SavingsAccount(id,name,balance);

        else if(type.equalsIgnoreCase("CURRENT"))
            return new CurrentAccount(id,name,balance);

        else
            return null;
    }
}
