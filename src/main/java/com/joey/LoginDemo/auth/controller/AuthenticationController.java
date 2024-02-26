package com.joey.LoginDemo.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joey.LoginDemo.auth.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService service;

	@PostMapping(value = "/register")
	public ResponseEntity<AuthenticationResponse> register (
			@RequestBody RegisterRequest request
			) {
		return ResponseEntity.ok(service.register(request));
	}
	
	@PostMapping(value = "/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate (
			@RequestBody AuthenticationRequest request
			) {
		return ResponseEntity.ok(service.authenticate(request));
	}
}
