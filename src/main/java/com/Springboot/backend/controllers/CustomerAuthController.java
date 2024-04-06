package com.Springboot.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Springboot.backend.config.CustomUsernamePasswordAthenticatonProvider;
import com.Springboot.backend.dto.CustomerDto;
import com.Springboot.backend.dto.LoginDto;
import com.Springboot.backend.entities.Customer;
import com.Springboot.backend.repository.CustomerRepository;
import com.Springboot.backend.services.SignupService;

import jakarta.validation.Valid;

@RestController
public class CustomerAuthController {
	
	@Autowired
	SignupService signService;
	
	@Autowired
	AuthenticationManager customAuthenticationManager;
	
	@Autowired
	CustomerRepository customerRepo;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody @Valid CustomerDto customerDto){
		return signService.customerSignup(customerDto);
		
	}
	
	@GetMapping("/test")
	public List<Customer> testController() {
		System.out.println("Hello");
		return customerRepo.findAll();
	}
	
	@GetMapping("/auth/test")
	public ResponseEntity<?> authTestController() {
		System.out.println("Hello");
		return ResponseEntity.ok(customerRepo.findAll());
	}
	
//	@PostMapping("/signin")
//	public ResponseEntity<String> customerLogin(@RequestBody LoginDto loginDto) throws Exception{
//		Authentication authObj;
//		
//		try {
//			
//			authObj=customAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
//			System.out.println(authObj);
//			SecurityContextHolder.getContext().setAuthentication(authObj);
//		}catch(Exception e) {
//			throw new Exception("Invaild credential"+ e.getMessage());
//		}
//		
//		test();
//		return ResponseEntity.status(HttpStatus.ACCEPTED).body("User login succsesfully");
//		
//	}
	
	
	@RequestMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication) {
        Customer customers = customerRepo.findByEmail(authentication.getName());
        if (customers!=null) {
            return customers;
        } else {
            return null;
        }

    }

}
