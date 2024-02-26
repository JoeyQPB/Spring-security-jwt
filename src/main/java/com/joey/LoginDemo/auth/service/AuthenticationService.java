package com.joey.LoginDemo.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.joey.LoginDemo.auth.controller.AuthenticationRequest;
import com.joey.LoginDemo.auth.controller.AuthenticationResponse;
import com.joey.LoginDemo.auth.controller.RegisterRequest;
import com.joey.LoginDemo.config.JwtService;
import com.joey.LoginDemo.domain.Role;
import com.joey.LoginDemo.domain.User;
import com.joey.LoginDemo.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private IUserRepository repository;
	private PasswordEncoder passwordencoder;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager; 
	
	@Autowired
	public AuthenticationService (
			IUserRepository repository,
			PasswordEncoder passwordencoder,
			JwtService jwtService,
			AuthenticationManager authenticationManager
			) {
		this.repository = repository;
		this.passwordencoder = passwordencoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		
	}

	public AuthenticationResponse register(RegisterRequest request) {
		String encodePassword = passwordencoder.encode(request.getPassword());
		User user = new User(
				request.getName(),
				request.getLastName(),
				request.getEmail(),
				encodePassword,
				Role.USER);
		
		repository.save(user);
		String jwtToken = jwtService.generateToken(user);
		return new AuthenticationResponse(jwtToken);
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
						)
				);
		
		User user = this.repository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("Error while get user from DB, not found!"));
		
		String jwtToken = jwtService.generateToken(user);
		return new AuthenticationResponse(jwtToken);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
