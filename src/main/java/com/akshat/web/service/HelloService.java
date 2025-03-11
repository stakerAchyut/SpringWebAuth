package com.akshat.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.akshat.web.model.User;
import com.akshat.web.repository.HelloRepository;

@Service
public class HelloService {
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

	private HelloRepository helloRepository;

	public HelloRepository getHelloRepository() {
		return helloRepository;
	}

	@Autowired
	public void setHelloRepository(HelloRepository helloRepository) {
		this.helloRepository = helloRepository;
	}
	
	public void registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		helloRepository.registerUser(user);
	}
	
	public List<User> getUsers() {
		return helloRepository.getUsers();
	}
}
