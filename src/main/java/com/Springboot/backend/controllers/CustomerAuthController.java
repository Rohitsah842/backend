package com.Springboot.backend.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Springboot.backend.config.CustomUsernamePasswordAthenticatonProvider;
import com.Springboot.backend.constants.SecurityConstants;
import com.Springboot.backend.customResponse.TokenResponse;
import com.Springboot.backend.dto.CustomerDto;
import com.Springboot.backend.dto.ForgetPasswordDto;
import com.Springboot.backend.dto.RefreshTokenDto;
import com.Springboot.backend.entities.Customer;
import com.Springboot.backend.entities.RefreshToken;
import com.Springboot.backend.exception.ExceptionBadRequest;
import com.Springboot.backend.exception.ExceptionUnAuthorized;
import com.Springboot.backend.repository.CustomerRepository;
import com.Springboot.backend.services.ForgetPasswordService;
import com.Springboot.backend.services.JwtService;
import com.Springboot.backend.services.RefreshTokenService;
import com.Springboot.backend.services.SignupService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class CustomerAuthController {
	
	@Autowired
	SignupService signService;
	
	@Autowired
	ForgetPasswordService forgetPasswordService;
	
	@Autowired
	AuthenticationManager customAuthenticationManager;
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	RefreshTokenService refreshTokenService;
	
	@Autowired
	JwtService jwtService;
	
	
	
	
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
	public List<Customer> authTestController() {
		return customerRepo.findAll();
	}
	
	
	
	@RequestMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication) {
        Customer customers = customerRepo.findByEmail(authentication.getName());
        if (customers!=null) {
            return customers;
        } else {
            return null;
        }

    }
	
	@PostMapping("/verify-user")
	public ResponseEntity<?>verifyUser(@RequestBody ForgetPasswordDto forgetPasswordDto ){
		
		return forgetPasswordService.verifyUserEmailOrMob(forgetPasswordDto.getEmail(), forgetPasswordDto.getMobileNo());
	}
	
	@PostMapping("/forget-password")
	public ResponseEntity<?> resetPassword(@RequestBody ForgetPasswordDto forgetPasswordDto){
		return forgetPasswordService.customerResetPassword(forgetPasswordDto);
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<?> generateRefreshToken(@RequestBody RefreshTokenDto refreshTokenDto, HttpServletResponse response){
		
		try {
			RefreshToken tokenId= refreshTokenService.findRefreshToken(refreshTokenDto.getToken());
			RefreshToken token=refreshTokenService.verifyRefreshToken(tokenId);
			String emailId=token.getCustomer().getEmail();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			System.out.println(authentication.getPrincipal()+" -"+authentication.getCredentials());
			String jwtToken= jwtService.createJwtToken(authentication);
			String newRefreshToken=refreshTokenService.createRefreshToken(emailId).getToken();
//			Cookie jwtCookie = new Cookie("Authorization",jwtToken);
//			jwtCookie.setPath("/");
//			jwtCookie.setMaxAge(SecurityConstants.JWT_EXPIRE_TIME);
//			jwtCookie.setSecure(true);
//			jwtCookie.setHttpOnly(true);
//			response.addCookie(jwtCookie);
//			
//			Cookie refreshTokenCookie = new Cookie("RefreshToken",newRefreshToken);
//			refreshTokenCookie.setPath("/");
////			refreshTokenCookie.setSecure(true);
////			refreshTokenCookie.setHttpOnly(true);
//			response.addCookie(refreshTokenCookie);
			TokenResponse tokenResponse=TokenResponse
			.builder()
			.jwtToken(jwtToken)
			.refreshToken(newRefreshToken)
			.build();
			return ResponseEntity.status(HttpStatus.OK).body( tokenResponse);
		}catch(Exception e) {
			throw new ExceptionUnAuthorized("Invaild refresh token id or expired");
		}
	}
		

}
