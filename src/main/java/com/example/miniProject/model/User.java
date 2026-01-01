package com.example.miniProject.model;

public class User {
    private String fullName;
    private String email;
    private String password; // Added password field
    private String securityPin; // For accounts table
    private double balance;

    // getters and setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSecurityPin() { return securityPin; }
    public void setSecurityPin(String securityPin) { this.securityPin = securityPin; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
