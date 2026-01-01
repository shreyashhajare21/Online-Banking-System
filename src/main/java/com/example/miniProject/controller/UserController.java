package com.example.miniProject.controller;

import com.example.miniProject.model.User;
import com.example.miniProject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            boolean success = userService.register(user);
            if (success) {
                model.addAttribute("message", "Registration successful!");
                return "login";
            } else {
                model.addAttribute("message", "Registration failed! Email may already be registered or there was a database error.");
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred during registration. Please try again later.");
            e.printStackTrace();
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String securityPin,
                        Model model) {
        try {
            boolean success = userService.login(email, securityPin);
            if (success) {
                model.addAttribute("userEmail", email);
                return "dashboard";
            } else {
                model.addAttribute("message", "Invalid credentials! Please check your email and security pin.");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred during login. Please try again later.");
            e.printStackTrace();
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam String userEmail, Model model) {
        model.addAttribute("userEmail", userEmail);
        return "dashboard";
    }

    @GetMapping("/check-balance")
    public String checkBalance(@RequestParam String userEmail, Model model) {
        double balance = userService.getBalanceByEmail(userEmail);
        model.addAttribute("userEmail", userEmail);
        if (balance >= 0) {
            model.addAttribute("message", "Your balance is: " + balance);
        } else {
            model.addAttribute("message", "Could not retrieve balance. Please try again.");
        }
        return "dashboard";
    }

    @GetMapping("/debit")
    public String showDebitForm(@RequestParam String userEmail, Model model) {
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("action", "debit");
        return "dashboard";
    }

    @PostMapping("/debit")
    public String debitMoney(@RequestParam String userEmail, @RequestParam double amount, Model model) {
        boolean success = userService.debit(userEmail, amount);
        model.addAttribute("userEmail", userEmail);
        if (success) {
            model.addAttribute("message", "Money debited successfully!");
        } else {
            model.addAttribute("message", "Debit failed! Insufficient balance or error.");
        }
        return "dashboard";
    }

    @GetMapping("/credit")
    public String showCreditForm(@RequestParam String userEmail, Model model) {
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("action", "credit");
        return "dashboard";
    }

    @PostMapping("/credit")
    public String creditMoney(@RequestParam String userEmail, @RequestParam double amount, Model model) {
        boolean success = userService.credit(userEmail, amount);
        model.addAttribute("userEmail", userEmail);
        if (success) {
            model.addAttribute("message", "Money credited successfully!");
        } else {
            model.addAttribute("message", "Credit failed! Please try again.");
        }
        return "dashboard";
    }

    @GetMapping("/transfer")
    public String showTransferForm(@RequestParam String userEmail, Model model) {
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("action", "transfer");
        return "dashboard";
    }

    @PostMapping("/transfer")
    public String transferMoney(@RequestParam String userEmail, @RequestParam String toEmail, @RequestParam double amount, Model model) {
        boolean success = userService.transfer(userEmail, toEmail, amount);
        model.addAttribute("userEmail", userEmail);
        if (success) {
            model.addAttribute("message", "Money transferred successfully!");
        } else {
            model.addAttribute("message", "Transfer failed! Insufficient balance or recipient not found.");
        }
        return "dashboard";
    }

    @GetMapping("/open-account")
    public String openAccount(@RequestParam String userEmail, Model model) {
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("message", "New account opened!");
        return "dashboard";
    }
}
