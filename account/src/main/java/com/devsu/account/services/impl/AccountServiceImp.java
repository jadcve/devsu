package com.devsu.account.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsu.account.entities.Account;
import com.devsu.account.repositories.AccountRepository;
import com.devsu.account.services.AccountService;

import jakarta.validation.Valid;

@Service
public class AccountServiceImp implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    public AccountServiceImp(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account registerAccount(@Valid Account account) {
        if(account == null){
            throw new IllegalArgumentException("Account is required");
        }
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    @Override
    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Account updateAccount(Long id, @Valid Account account) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        existingAccount.setNumero(account.getNumero());
        existingAccount.setTipo(account.getTipo());
        existingAccount.setSaldo(account.getSaldo());
        existingAccount.setEstado(account.isEstado());

        return accountRepository.save(existingAccount);
    }

}
