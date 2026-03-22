package ui;

import factory.AccountFactory;
import java.util.Scanner;
import model.Account;
import repository.AccountRepository;
import service.BankingService;

public class BankingApplication {

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {
            AccountRepository repo = new AccountRepository();
            BankingService service = new BankingService(repo);

            while(true){

                System.out.println("\n1.Create Account");
                System.out.println("2.Deposit");
                System.out.println("3.Withdraw");
                System.out.println("4.Transfer");
                System.out.println("5.Check Balance");
                System.out.println("6.Exit");

                int choice = sc.nextInt();

                try{

                    switch(choice){

                        case 1 -> {
                            System.out.print("Enter type (SAVINGS/CURRENT): ");
                            String type=sc.next();

                            System.out.print("ID: ");
                            int id=sc.nextInt();

                            System.out.print("Name: ");
                            String name=sc.next();

                            System.out.print("Balance: ");
                            double bal=sc.nextDouble();

                            Account acc =
                                    AccountFactory.createAccount(type,id,name,bal);

                            service.createAccount(acc);

                            System.out.println("Account Created");
                        }

                        case 2 -> {
                            System.out.print("ID: ");
                            int id = sc.nextInt();
                            System.out.print("Amount: ");
                            double amt=sc.nextDouble();
                            service.deposit(id,amt);
                            System.out.println("Deposit successful");
                        }

                        case 3 -> {
                            System.out.print("ID: ");
                            int id = sc.nextInt();
                            System.out.print("Amount: ");
                            double amt = sc.nextDouble();
                            service.withdraw(id,amt);
                            System.out.println("Withdraw successful");
                        }

                        case 4 -> {
                            System.out.print("From ID: ");
                            int from=sc.nextInt();
                            System.out.print("To ID: ");
                            int to=sc.nextInt();
                            System.out.print("Amount: ");
                            double amt = sc.nextDouble();
                            service.transfer(from,to,amt);
                            System.out.println("Transfer successful");
                        }

                        case 5 -> {
                            System.out.print("ID: ");
                            int id = sc.nextInt();
                            System.out.print(
                                    "Balance:  "+service.getBalance(id));
                        }

                        case 6 -> System.exit(0);
                    }

                }catch(Exception e){
                    System.out.println("Error: "+e.getMessage());
                }
            }
        }
    }
}
