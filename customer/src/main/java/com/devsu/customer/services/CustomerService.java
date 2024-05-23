package com.devsu.customer.services;


import com.devsu.customer.dtos.CustomerResponseDTO;
import com.devsu.customer.entities.Customer;


public interface CustomerService {
    Customer registerCustomer(Customer customer);
    CustomerResponseDTO updateCustomer(Long id, Customer customerDetails);
    void deleteCustomer(Long id);
    CustomerResponseDTO getCustomer(Long id);
}
