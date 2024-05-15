package com.devsu.customer.repositories;

import com.devsu.customer.entities.Person;
import org.springframework.data.repository.CrudRepository;


public interface PersonRepository extends CrudRepository<Person, Long>{

    
} 