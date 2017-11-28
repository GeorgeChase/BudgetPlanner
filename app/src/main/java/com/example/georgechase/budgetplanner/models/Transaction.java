package com.example.georgechase.budgetplanner.models;

public class Transaction {
    private String date;
    private String category;
    private String amount;

    public Transaction(String date, String category, String amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public Transaction() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date='" + date + '\'' +
                ", category='" + category + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
