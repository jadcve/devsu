package com.devsu.account.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.devsu.account.dtos.CustomerDTO;


@Service
public class CustomerServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    public CustomerDTO getCustomerById(Long customerId) {
        String url = customerServiceUrl + "/clientes/obtener/" + customerId;
        CustomerDTO customerDTO = restTemplate.getForObject(url, CustomerDTO.class);
        System.out.println("CustomerDTO: " + customerDTO); // Debugging
        return customerDTO;
    }
}
