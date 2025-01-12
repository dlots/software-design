package org.example.BankAccount;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {
    void checkBalanceIsPositive(BankAccount account) {
        final double balance = account.getBalance();
        assertTrue(balance >= 0, "Account has negative balance %f".formatted(balance));
    }

    @Test
    void createAccount() {
        Arrays.asList(0, 5, -5).forEach(initialValue -> {
            System.out.printf("creating account with initial value %d%n", initialValue);
            BankAccount account = new BankAccount(initialValue);
            checkBalanceIsPositive(account);
        });
    }

    @Test
    void deposit() {
        Arrays.asList(0, 100).forEach(initialValue -> {
            Arrays.asList(0, 5, -5).forEach(valueToDeposit -> {
                System.out.printf("depositing %d to account with value %d%n", valueToDeposit, initialValue);
                BankAccount account = new BankAccount(initialValue);
                account.deposit(valueToDeposit);
                checkBalanceIsPositive(account);
                assertFalse(account.getBalance() < initialValue, "Account balance is fewer after deposit");
                assertEquals(account.getBalance(), initialValue + valueToDeposit);
            });
        });
    }

    @Test
    void withdraw() {
        Arrays.asList(0, 100).forEach(initialValue -> {
            Arrays.asList(0, 5, -5).forEach(valueToWithdraw -> {
                System.out.printf("withdrawing %d from account with value %d%n", valueToWithdraw, initialValue);
                BankAccount account = new BankAccount(initialValue);
                account.withdraw(valueToWithdraw);
                checkBalanceIsPositive(account);
                assertFalse(account.getBalance() > initialValue, "Account balance is more after deposit");
                assertEquals(account.getBalance(), initialValue - valueToWithdraw);
            });
        });
    }
}