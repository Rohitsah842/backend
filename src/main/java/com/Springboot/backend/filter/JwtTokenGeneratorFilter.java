package com.Springboot.backend.filter;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Springboot.backend.constants.SecurityConstants;
import com.Springboot.backend.exception.ExceptionForbidden;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {

            String jwt = Jwts.builder()
            		.setIssuer("Eazy Bank")
            		.setSubject("JWT Token")
                   .claim("username", authentication.getName()) 
//                    .claim("authorities", authentication.getAuthorities())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 24*60*60*000))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
            System.out.println(jwt);
            response.setHeader(SecurityConstants.JWT_HEADER, jwt);
        }else {
        	throw new ExceptionForbidden("Email or password Invalid ");
        }

        filterChain.doFilter(request, response);
	}
	
	 @Override
	    protected boolean shouldNotFilter(HttpServletRequest request) {
	        return !request.getServletPath().equals("/user");
	    }
	
//	private String populateAuthorities(String role) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//		authorities.add(new SimpleGrantedAuthority(role));
//        return String.join(",", authorities);
//    }
	
private Key getSignKey() {
		
		byte[] keyBytes=Decoders.BASE64.decode(SecurityConstants.JWT_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
