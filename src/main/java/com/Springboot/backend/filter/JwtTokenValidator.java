package com.Springboot.backend.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Springboot.backend.constants.SecurityConstants;
import com.Springboot.backend.exception.ExceptionBadRequest;
import com.Springboot.backend.exception.ExceptionUnAuthorized;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader(SecurityConstants.JWT_HEADER);
		if (token != null) {
			try {
				Claims claims = Jwts.parserBuilder()
		                .setSigningKey(getSignKey())
		                .build()
		                .parseClaimsJws(token)
		                .getBody();
				
                String username = String.valueOf(claims.get("username"));
				String authorities = (String) claims.get("authorities");
//				System.out.println(username+" "+" "+authorities );

				Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
						AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)); // AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (Exception e) {
				throw new ExceptionUnAuthorized(e.getMessage());
			}
		} else {
			throw new ExceptionUnAuthorized("UNAUTHRIZED");
		}
		filterChain.doFilter(request, response);

	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !request.getServletPath().contains("/auth/");
	}

	private Key getSignKey() {

		byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.JWT_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

//	private Claims extractAllClaims(String token) {
//		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
//	}
//	
//	public String extractUsername(String token) {
//		return extractClaim(token,Claims::getSubject);
//	}
//
//	private <T> T extractClaim(String token, Function<Claims,T>claimsResolver ) {
//		final Claims claims=extractAllClaims(token);
//		
//		return claimsResolver.apply(claims);
//	}

}
