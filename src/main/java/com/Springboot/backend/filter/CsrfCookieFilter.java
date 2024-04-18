package com.Springboot.backend.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Springboot.backend.entities.Customer;
import com.Springboot.backend.repository.CustomerRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CsrfCookieFilter extends OncePerRequestFilter {
    

	 @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	            throws ServletException, IOException {
		 
	        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
	        if(null != csrfToken.getHeaderName()){
	            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
	        }
	        filterChain.doFilter(request, response);
	    }

}
