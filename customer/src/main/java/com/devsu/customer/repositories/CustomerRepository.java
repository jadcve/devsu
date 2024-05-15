package com.devsu.customer.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.devsu.customer.entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{

    @SuppressWarnings({ "null", "unchecked" })
    Customer save(Customer customer);
    Optional<Customer> findByCustomerId(int i);

}
