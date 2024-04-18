package com.Springboot.backend.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Springboot.backend.entities.Customer;
import com.Springboot.backend.exception.ExceptionBadRequest;
import com.Springboot.backend.exception.ExceptionForbidden;
import com.Springboot.backend.repository.CustomerRepository;
import com.Springboot.backend.services.RefreshTokenService;


@Component
public class CustomUsernamePasswordAthenticatonProvider implements AuthenticationProvider{
	
	@Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    

	@Override
	public Authentication authenticate(Authentication authentication) throws ExceptionForbidden {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		Customer customer = customerRepository.findByEmail(username);
		

		if (customer!=null) {
			if (passwordEncoder.matches(password, customer.getPassword())) {
				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority(customer.getRole()));
				return new UsernamePasswordAuthenticationToken(username, password, authorities);
				
			}else  {
				throw new ExceptionForbidden("Email or password Invalid ");
			}
		} else {
			throw new ExceptionForbidden("No user registered with this details");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
   
	
										//Set<Authority> authorities
//	    private List<GrantedAuthority> getGrantedAuthorities() {
//	        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
////	        for (Authority authority : authorities) {
////	            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
////	        }
//	        return grantedAuthorities;
//	    }

//	    @Override
//	    public boolean supports(Class<?> authentication) {
//	        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//	    }


}
