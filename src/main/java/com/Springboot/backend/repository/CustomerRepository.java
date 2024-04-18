package com.Springboot.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Springboot.backend.dto.CustomerDto;
import com.Springboot.backend.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	Customer findByEmail(String email);
	Customer findOneByEmailAndPassword(String email, String password);
	Customer findByEmailOrMobileNo(String email, String string);

}
