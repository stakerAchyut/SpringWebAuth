package com.akshat.web.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.akshat.web.model.User;
import com.akshat.web.service.HelloService;
import com.akshat.web.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HelloController {
	
	private HelloService helloService;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager;

	public HelloService getHelloService() {
		return helloService;
	}

	@Autowired
	public void setHelloService(HelloService helloService) {
		this.helloService = helloService;
	}

	public JwtService getJwtService() {
		return jwtService;
	}

	@Autowired
	public void setJwtService(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@PostMapping(path = "register")
	public void register(@RequestBody User user) {
		helloService.registerUser(user);
	}
	
	@PostMapping(path = "login")
	public String login(HttpServletRequest request) {
		try {
			String authHeader = (String) request.getHeader("Authorization");
			String username = null;
			String password = null;
			if (authHeader != null && authHeader.startsWith("Basic")) {
				String decoded = new String(Base64.getDecoder().decode(authHeader.substring(6)));
				username = decoded.split(":")[0];
				password = decoded.split(":")[1];
			}
			
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			if (authentication.isAuthenticated()) {
				return jwtService.generateToken(username);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Login Failed !!!";
	}
	
	@GetMapping(path = "users")
	public List<User> getUsers() {
		return helloService.getUsers();
	}
	
	@GetMapping(path = "/")
	public String hello(HttpServletRequest request) {
		return "Hello Akshat !!! " + request.getSession().getId();
	}
}
