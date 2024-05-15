package com.devsu.customer.services;

import com.devsu.customer.entities.Customer;


public interface CustomerService {
    Customer registerCustomer(Customer customer);
    Customer getCustomer(Long id);
    Customer updateCustomer(Long id, Customer customerDetails);
    void deleteCustomer(Long id);
    Customer listarCustomer();
}
