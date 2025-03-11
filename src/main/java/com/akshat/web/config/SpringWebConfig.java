package com.akshat.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringWebConfig {
	
	private UserDetailsService userDetailsService;
	private JwtFilter jwtFilter;

    public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

    @Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

    public JwtFilter getJwtFilter() {
		return jwtFilter;
	}

    @Autowired
	public void setJwtFilter(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	// Used for disabling the csrf token and make all the http requests stateless
    // Also, used for permitting register API without any authentication
	@Bean
    SecurityFilterChain secureHttpRequest(HttpSecurity http) throws Exception {
    	http.csrf(customizer -> customizer.disable())
    		.authorizeHttpRequests(request -> request
    				.requestMatchers("register","login")
    				.permitAll()
    				.anyRequest().authenticated())
    		.httpBasic(Customizer.withDefaults())
    		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    	
		return http.build();
	}
	
	// Used for overriding the userDetailsService for setting it through DB for authentication
	@Bean
    AuthenticationProvider authenticate() {
    	DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    	authenticationProvider.setUserDetailsService(userDetailsService);
    	authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
    	return authenticationProvider;
    }
	
	// Used for getting the authentication manager bean for validating authentication in Controller
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
    
	// Used for manually configure the userDetailsService for authentication
    /*@Bean
    UserDetailsService setupUserDetails() {
    	List<UserDetails> userDetailsList = new ArrayList<>();
    	userDetailsList.add(User.builder()
    			.username("Tanmay")
    			.password("{noop}T@123")
    			.roles("USER")
    			.build());
    	userDetailsList.add(User.builder()
    			.username("Saurav")
    			.password("{noop}S@123")
    			.roles("USER")
    			.build());
    	
    	return new InMemoryUserDetailsManager(userDetailsList);
    }*/
    
}
