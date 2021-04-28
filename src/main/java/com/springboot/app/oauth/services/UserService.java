package com.springboot.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.springboot.app.oauth.clients.FeignUserClient;

import brave.Tracer;
import feign.FeignException;

/**
 * Class used to load the user by rest client "FeignClient"
 * @author Usuario
 *
 */
@Service
public class UserService implements UserDetailsService, IUserService {
	
	private Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private FeignUserClient client;
	
	@Autowired
	private Tracer tracer;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		try {
			
		com.springboot.app.commons.users.models.entity.User user = client.findByUsername(username);
		
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> log.info(authority.getAuthority()))
				.collect(Collectors.toList());
		
		log.info("User authenticated {}", username);
		
		return new User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities);
		
		}catch (FeignException e) {
			log.error("User not found does not exist {}", username);
			tracer.currentSpan().tag("error.message", e.getMessage());
			throw new UsernameNotFoundException("User does not exist " + username);
		}		
	}

	@Override
	public com.springboot.app.commons.users.models.entity.User findByUsername(String username) {
		return client.findByUsername(username);
	}

}
