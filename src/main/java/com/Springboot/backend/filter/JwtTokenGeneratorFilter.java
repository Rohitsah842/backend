package com.Springboot.backend.filter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Springboot.backend.constants.SecurityConstants;
import com.Springboot.backend.services.JwtService;
import com.Springboot.backend.services.RefreshTokenService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	public JwtService jwtService;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 
        if (null != authentication) {

//            String jwt = Jwts.builder()
//            		.setIssuer("Eazy Bank")
//            		.setSubject("JWT Token")
//                   .claim("username", authentication.getName()) 
////                    .claim("authorities", authentication.getAuthorities())
//                    .setIssuedAt(new Date())
//                    .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRE_TIME))
//                    .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        	String jwtToken=jwtService.createJwtToken(authentication);
            String refreshToken=refreshTokenService.createRefreshToken(authentication.getName()).getToken();
            System.out.println(refreshToken);
            response.setHeader(SecurityConstants.JWT_HEADER, jwtToken);
//            response.setHeader(SecurityConstants.REFRESH_TOKEN, refreshToken);
            response.addHeader(SecurityConstants.REFRESH_TOKEN, refreshToken);
        }else {
        	response.sendError(401, "Email id or password is incorrect");
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
//        return String.join(',', authorities);
//    }
	
//private Key getSignKey() {
//		
//		byte[] keyBytes=Decoders.BASE64.decode(SecurityConstants.JWT_KEY);
//		return Keys.hmacShaKeyFor(keyBytes);
//	}

}
