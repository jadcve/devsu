package com.devsu.account.services;

import java.util.List;

import com.devsu.account.entities.Account;

public interface AccountService {

    Account registerAccount(Account account);
    Account getAccountById(Long id);
    Account updateAccount(Long id, Account account);
    void deleteAccount(Long id);
    List<Account> getAllAccounts();
    
}
