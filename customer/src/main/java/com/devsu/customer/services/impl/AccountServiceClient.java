package com.devsu.customer.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.devsu.customer.dtos.AccountDTO;
import com.devsu.customer.exceptions.AccountNotFoundException;

@Service
public class AccountServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    public AccountDTO getAccountById(Long accountId) {
        String url = accountServiceUrl + "/cuentas/" + accountId;
        try {
            return restTemplate.getForObject(url, AccountDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new AccountNotFoundException("Account not found for ID: " + accountId);
        }
    }
}