package com.example.miniProject.dao;

import com.example.miniProject.model.User;
import org.springframework.stereotype.Repository;
import java.sql.*;

@Repository
public class UserDAO {

    private final String url = "jdbc:mysql://localhost:3306/banking_system";
    private final String username = "root";
    private final String password = "Shreyash@21";

    public boolean register(User user) {
        String sqlAccounts = "INSERT INTO accounts(full_name, email, security_pin, balance) VALUES (?, ?, ?, ?)";
        String sqlUser = "INSERT INTO user(full_name, email, password) VALUES (?, ?, ?)";
        Connection con = null;
        PreparedStatement psAccounts = null;
        PreparedStatement psUser = null;
        try {
            con = DriverManager.getConnection(url, username, password);
            con.setAutoCommit(false); // Start transaction

            psAccounts = con.prepareStatement(sqlAccounts);
            psAccounts.setString(1, user.getFullName());
            psAccounts.setString(2, user.getEmail());
            psAccounts.setString(3, user.getSecurityPin()); // Use securityPin for accounts
            psAccounts.setDouble(4, user.getBalance());
            int accountsResult = psAccounts.executeUpdate();

            psUser = con.prepareStatement(sqlUser);
            psUser.setString(1, user.getFullName());
            psUser.setString(2, user.getEmail());
            psUser.setString(3, user.getPassword()); // Use password for user table
            int userResult = psUser.executeUpdate();

            if (accountsResult > 0 && userResult > 0) {
                con.commit();
                return true;
            } else {
                con.rollback();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (psAccounts != null) psAccounts.close();
                if (psUser != null) psUser.close();
                if (con != null) con.setAutoCommit(true);
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean login(String email, String securityPinInput) {
        String sql = "SELECT * FROM accounts WHERE email = ? AND security_pin = ?";
        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, securityPinInput);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getBalanceByEmail(String email) {
        String sql = "SELECT balance FROM accounts WHERE email = ?";
        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean debit(String email, double amount) {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE email = ? AND balance >= ?";
        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, email);
            ps.setDouble(3, amount);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean credit(String email, double amount) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE email = ?";
        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean transfer(String fromEmail, String toEmail, double amount) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, username, password);
            con.setAutoCommit(false);
            String debitSql = "UPDATE accounts SET balance = balance - ? WHERE email = ? AND balance >= ?";
            String creditSql = "UPDATE accounts SET balance = balance + ? WHERE email = ?";
            try (PreparedStatement debitPs = con.prepareStatement(debitSql);
                 PreparedStatement creditPs = con.prepareStatement(creditSql)) {
                debitPs.setDouble(1, amount);
                debitPs.setString(2, fromEmail);
                debitPs.setDouble(3, amount);
                int debitResult = debitPs.executeUpdate();
                if (debitResult == 0) {
                    con.rollback();
                    return false;
                }
                creditPs.setDouble(1, amount);
                creditPs.setString(2, toEmail);
                int creditResult = creditPs.executeUpdate();
                if (creditResult == 0) {
                    con.rollback();
                    return false;
                }
                con.commit();
                return true;
            }
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean openAccount(User user) {
        return register(user);
    }
}
