package com.akshat.web.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.akshat.web.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private JwtService jwtService;
	private UserDetailsService userDetailsService;
	
	public JwtService getJwtService() {
		return jwtService;
	}

	@Autowired
	public void setJwtService(JwtService jwtService) {
		this.jwtService = jwtService;
	}
	
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

    @Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = (String) request.getHeader("Authorization");
		
		String token = null;
		String username = null;
		
		
		System.out.println("doFilterInternal authHeader::" + authHeader);
		if (authHeader != null && authHeader.startsWith("Bearer")) {
			token = authHeader.substring(7);
			username = jwtService.extractUsername(token);
		}
		System.out.println("doFilterInternal username::" + username);
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			System.out.println("doFilterInternal userDetails::" + userDetails.getUsername());
			
			if (jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
