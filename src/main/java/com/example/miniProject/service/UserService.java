package com.example.miniProject.service;

import com.example.miniProject.dao.UserDAO;
import com.example.miniProject.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean register(User user) {
        return userDAO.register(user);
    }

    public boolean login(String email, String password) {
        return userDAO.login(email, password);
    }

    public double getBalanceByEmail(String email) {
        return userDAO.getBalanceByEmail(email);
    }

    public boolean debit(String email, double amount) {
        return userDAO.debit(email, amount);
    }

    public boolean credit(String email, double amount) {
        return userDAO.credit(email, amount);
    }

    public boolean transfer(String fromEmail, String toEmail, double amount) {
        return userDAO.transfer(fromEmail, toEmail, amount);
    }

    public boolean openAccount(User user) {
        return userDAO.openAccount(user);
    }
}
