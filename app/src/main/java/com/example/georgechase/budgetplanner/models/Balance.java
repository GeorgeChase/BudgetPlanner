package com.example.georgechase.budgetplanner.models;

public class Balance {
    private String amount;

    public Balance(String balance) {
        this.amount = balance;
    }

    public Balance() {
    }

    public String getBalance() {
        return amount;
    }

    public void setBalance(String balance) {
        this.amount = balance;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "balance='" + amount + '\'' +
                '}';
    }
}