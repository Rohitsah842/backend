package com.Springboot.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Springboot.backend.dto.ForgetPasswordDto;
import com.Springboot.backend.entities.Customer;
import com.Springboot.backend.exception.ExceptionBadRequest;
import com.Springboot.backend.exception.ExceptionForbidden;
import com.Springboot.backend.repository.CustomerRepository;

@Service
public class ForgetPasswordService {
	
	@Autowired
	public CustomerRepository customerRepo;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public ResponseEntity<?> verifyUserEmailOrMob(String email, String mob) {
		Customer customer=null;

		if(email!="" || mob!="") {
			 customer= customerRepo.findByEmailOrMobileNo(email, mob);
		}
		
		if(customer!=null) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("User verified sucessfully");
		}else {
			throw new ExceptionBadRequest("Email or Mobile no is invaid");
		}
		
		
	}

	public ResponseEntity<?> customerResetPassword(ForgetPasswordDto forgetPasswordDto) {
		Customer customer=null;
		if(!forgetPasswordDto.getNewPassword().equalsIgnoreCase(forgetPasswordDto.getConfirmPassword())) {
			throw new ExceptionForbidden("Password and confirm password not matched");
		}
		
		String hashPwd= passwordEncoder.encode(forgetPasswordDto.getNewPassword());
		
		if(forgetPasswordDto.getEmail()!="" || forgetPasswordDto.getMobileNo()!="") {
			 customer= customerRepo.findByEmailOrMobileNo(forgetPasswordDto.getEmail(), forgetPasswordDto.getMobileNo());
			 customer.setPassword(hashPwd);
		}

		customerRepo.save(customer);
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Password changed sucessfully");
	}

}
