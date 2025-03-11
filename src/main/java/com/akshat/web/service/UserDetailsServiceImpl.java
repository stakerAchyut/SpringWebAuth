package com.akshat.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.akshat.web.model.User;
import com.akshat.web.model.UserPrincipal;
import com.akshat.web.repository.HelloRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	HelloRepository helloRepository;

	public HelloRepository getHelloRepository() {
		return helloRepository;
	}

	@Autowired
	public void setHelloRepository(HelloRepository helloRepository) {
		this.helloRepository = helloRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<User> usersList = helloRepository.getUsers();
		User user = usersList.stream()
				.filter(name -> name.getUsername().equals(username))
				.findFirst()
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found : " + username));
		return new UserPrincipal(user);
	}

}
