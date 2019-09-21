package com.example.test1.generics;

public class FundsRequest extends GenericMenuActivity {
    private int id;
    private String personName;
    private long accountNumber;
    private int amount;
    private long phoneNumber;
    private String purpose;

    public FundsRequest(int id, String personName, long accountNumber, int amount, long phoneNumber, String purpose) {
        this.id = id;
        this.personName = personName;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.phoneNumber = phoneNumber;
        this.purpose = purpose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
