package com.Springboot.backend.services;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.Springboot.backend.constants.SecurityConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	public String createJwtToken(Authentication authentication) {
		String jwt = Jwts.builder()
        		.setIssuer("Eazy Bank")
        		.setSubject("JWT Token")
               .claim("username", authentication.getName()) 
//                .claim("authorities", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + SecurityConstants.JWT_EXPIRE_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
		return jwt;
	}
	
private Key getSignKey() {
		
		byte[] keyBytes=Decoders.BASE64.decode(SecurityConstants.JWT_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
