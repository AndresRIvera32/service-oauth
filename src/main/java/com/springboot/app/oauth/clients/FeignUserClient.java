package com.springboot.app.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.app.commons.users.models.entity.User;

@FeignClient(name = "users-service")
public interface FeignUserClient {

	@GetMapping("/users/search/search-username")
	public User findByUsername(@RequestParam String username);
	
}
