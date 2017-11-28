package com.example.georgechase.budgetplanner.models;

public class Goal {
    private String date;
    private String category;
    private String required_amount;

    public Goal(String date, String category, String required_amount) {
        this.date = date;
        this.category = category;
        this.required_amount = required_amount;
    }

    public Goal() {
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

    public String getRequired_amount() {
        return required_amount;
    }

    public void setRequired_amount(String required_amount) {
        this.required_amount = required_amount;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "date='" + date + '\'' +
                ", category='" + category + '\'' +
                ", required_amount='" + required_amount + '\'' +
                '}';
    }
}
