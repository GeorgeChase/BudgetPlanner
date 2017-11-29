package com.example.georgechase.budgetplanner.models;

public class Transaction {
    private String date;
    private String itemName;
    private String amount;

    public Transaction(String date, String itemName, String amount) {
        this.date = date;
        this.itemName = itemName;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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
                ", itemName='" + itemName + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
