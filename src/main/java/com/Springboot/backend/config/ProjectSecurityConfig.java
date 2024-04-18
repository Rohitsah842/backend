package com.Springboot.backend.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.Springboot.backend.filter.CsrfCookieFilter;
import com.Springboot.backend.filter.JwtTokenGeneratorFilter;
import com.Springboot.backend.filter.JwtTokenValidator;
import com.Springboot.backend.filter.RequestValidationBeforeFilter;
import com.Springboot.backend.services.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ProjectSecurityConfig {
	
	@Autowired
	private CsrfCookieFilter csrfCookieFilter;
	
	@Autowired
	private JwtTokenGeneratorFilter jwtTokenGeneratorFilter;
	
	@Autowired
	private JwtTokenValidator jwtTokenValidator;
	
	
	
	 @Bean
	    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
	        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
	        requestHandler.setCsrfRequestAttributeName("_csrf");
	        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(Arrays.asList("Authorization", "RefreshToken"));
                config.setMaxAge(3600L);
                return config;
            }
                })).csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers("/forget-password","/signup", "/test", "/verify-user", "/refresh-token")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter( csrfCookieFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
//                .addFilterAt(new AuthoritiesLoggingAtFilter(),BasicAuthenticationFilter.class)
//                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter( jwtTokenGeneratorFilter, BasicAuthenticationFilter.class)
                .addFilterBefore( jwtTokenValidator, BasicAuthenticationFilter.class)
	            			.authorizeHttpRequests((requests)->requests
	                        .requestMatchers("/user", "/auth/test").authenticated()
	                        .requestMatchers("/verify-user","/signup","/signin", "/test","/forget-password", "/refresh-token").permitAll())
	                .formLogin(Customizer.withDefaults())
	                .httpBasic(Customizer.withDefaults());
	        return http.build();
		 
	    }
	 

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
			return config.getAuthenticationManager();
		}
	    
//	    .csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers("/signup","/login")
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//        .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
//        .addFilterAt(new AuthoritiesLoggingAtFilter(),BasicAuthenticationFilter.class)
//        .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
//        .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
//        .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
//        .authorizeHttpRequests((requests)->requests
//                .requestMatchers("/myAccount").hasRole("USER")
//                .requestMatchers("/myBalance").hasAnyRole("USER","ADMIN")
//                .requestMatchers("/myLoans").hasRole("USER")
//                .requestMatchers("/myCards").hasRole("USER")

}
