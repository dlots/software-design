package org.example.bank;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.function.Executable;
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
            try {
                BankAccount account = BankAccount.createAccount(initialValue);
                checkBalanceIsPositive(account);
            } catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        });
    }

    @Test
    void deposit() {
        Arrays.asList(0, 100).forEach(initialValue -> {
            Arrays.asList(0, 5, -5).forEach(valueToDeposit -> {
                System.out.printf("depositing %d to account with value %d%n", valueToDeposit, initialValue);
                BankAccount account = BankAccount.createAccount(initialValue);
                final Executable depositCall = () -> account.deposit(valueToDeposit);
                if (valueToDeposit == -5) {
                    assertThrows(IllegalArgumentException.class, depositCall);
                    return;
                } else {
                    assertDoesNotThrow(depositCall);
                }
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
                BankAccount account = BankAccount.createAccount(initialValue);
                final Executable withdrawCall = () -> account.withdraw(valueToWithdraw);
                if (initialValue == 0 && valueToWithdraw == 5 || valueToWithdraw == -5) {
                    assertThrows(IllegalArgumentException.class, withdrawCall);
                    return;
                } else {
                    assertDoesNotThrow(withdrawCall);
                }
                checkBalanceIsPositive(account);
                assertFalse(account.getBalance() > initialValue, "Account balance is more after withdraw");
                assertEquals(account.getBalance(), initialValue - valueToWithdraw);
            });
        });
    }
}