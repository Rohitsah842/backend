package com.Springboot.backend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Springboot.backend.dto.CustomerDto;
import com.Springboot.backend.entities.Customer;
import com.Springboot.backend.exception.ExceptionBadRequest;
import com.Springboot.backend.exception.ExceptionForbidden;
import com.Springboot.backend.exception.ExceptionUnAuthorized;
import com.Springboot.backend.repository.CustomerRepository;

@Service
public class SignupService {
	
	@Autowired
	public CustomerRepository customerRepo;
	

	
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
//	@Autowired
//	private Customer customer;
	
	public ResponseEntity<?> customerSignup(CustomerDto customerDto) throws ExceptionForbidden{
		Customer saveCustomer=null;
		Customer customer= new Customer();
		ResponseEntity<?> response=null;
		Customer existsCustomer= customerRepo.findByEmail(customerDto.getEmail());
		
		if(existsCustomer==null) {
		
		try {
			
			String hashPwd= passwordEncoder.encode(customerDto.getPassword());
			System.out.println(hashPwd);
			customer.setCreateDate(String.valueOf(new Date(System.currentTimeMillis())));
			customer.setPassword(hashPwd);
			customer.setFullname(customerDto.getFullname());
			customer.setEmail(customerDto.getEmail());
			customer.setMobileNo(customerDto.getMobileNo());
			customer.setAge(customerDto.getAge());
			customer.setRole(customerDto.getRole());
			saveCustomer=customerRepo.save(customer);
			if(saveCustomer.getId()>0) {
				System.out.println(saveCustomer);
				response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Given user details are successfully registered");
			}
			
		}catch(Exception ex) {
			 throw  new ExceptionBadRequest(ex.getMessage());
		}
		}else {
			throw new ExceptionBadRequest("User id already exists");
//			
		}
		
		return response;		
		
	}
	

}
