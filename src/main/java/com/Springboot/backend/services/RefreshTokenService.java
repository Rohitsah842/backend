package com.Springboot.backend.services;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.Springboot.backend.constants.SecurityConstants;
import com.Springboot.backend.entities.RefreshToken;
import com.Springboot.backend.exception.ExceptionBadRequest;
import com.Springboot.backend.repository.CustomerRepository;
import com.Springboot.backend.repository.RefreshTokenRepository;

@Component
public class RefreshTokenService {
	
	
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepo;
	
	


	public RefreshToken createRefreshToken(String userName) {
		
		RefreshToken refreshToken=RefreshToken.builder()
								.customer(customerRepo.findByEmail(userName))
								.token(UUID.randomUUID().toString())
								.expireDate(Instant.now().plusMillis(SecurityConstants.REFRESHTOKEN_EXPIRE_TIME))
								.build();
		RefreshToken existingRefreshToken=refreshTokenRepo.findByCustomer(customerRepo.findByEmail(userName));
		if(existingRefreshToken!=null) {
			refreshTokenRepo.delete(existingRefreshToken);
		}
		return refreshTokenRepo.save(refreshToken);
	}
	

	public RefreshToken findRefreshToken(String token) {
		RefreshToken refreshToken= refreshTokenRepo.findByToken(token);
		return refreshToken;
	}
	
	public RefreshToken verifyRefreshToken(RefreshToken token){
		if(token.getExpireDate().compareTo(Instant.now())<0) {
			refreshTokenRepo.delete(token);
			throw new ExceptionBadRequest(" Refresh token was expired. Please make a new signin request");
		}
		return token;
	}
	

}
