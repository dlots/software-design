package org.example.BankAccount;

class BankAccount {
    private double balance;

    private BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    static BankAccount createEmptyAccount() {
        return new BankAccount(0);
    }

    static void verifyAmount(double amount) throws IllegalArgumentException{
        if (amount < 0) {
            throw new IllegalArgumentException("Negative amount %f is not allowed".formatted(amount));
        }
    }

    static BankAccount createAccount(double initialBalance) throws IllegalArgumentException {
        verifyAmount(initialBalance);
        return new BankAccount(initialBalance);
    }

    public void deposit(double amount) throws IllegalArgumentException {
        verifyAmount(amount);
        balance += amount;
    }

    public void withdraw(double amount) throws IllegalArgumentException {
        verifyAmount(amount);
        double newBalance = balance - amount;
        verifyAmount(newBalance);
        balance = newBalance;
    }

    public double getBalance() {
        return balance;
    }
}
