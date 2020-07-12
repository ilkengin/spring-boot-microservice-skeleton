package com.ilkengin.core.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.ilkengin.core.gateway.dto.LoginRequest;
import com.ilkengin.core.gateway.dto.LoginResponse;
import com.ilkengin.core.gateway.dto.SignUpRequest;
import com.ilkengin.core.gateway.error.EmailExistsException;
import com.ilkengin.core.gateway.error.UsernameExistsException;
import com.ilkengin.core.gateway.provider.JwtTokenProvider;
import com.ilkengin.core.gateway.service.AuthenticationService;


@RestController
@RequestScope
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody @Validated LoginRequest loginRequest) {
		try {
			var authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
			var authentication = authenticationManager.authenticate(authToken);
			var token = jwtTokenProvider.createToken(authentication);
			return ResponseEntity.ok(new LoginResponse(token));
		} catch (Exception e) {
			System.err.println("Log in failed for user " + loginRequest.getUsername());
		}
		return ResponseEntity.badRequest().body("Username or password is incorrect");
	}
	
	@PostMapping("/signup")
	public ResponseEntity<String> register(@RequestBody @Validated SignUpRequest signUpRequest) {
		
		try {
			authenticationService.createUser(signUpRequest);
		} catch(UsernameExistsException e) {
            return new ResponseEntity<String>("Username is already taken!", HttpStatus.BAD_REQUEST);
		} catch(EmailExistsException e) {
			return new ResponseEntity<String>("Email is already in use!", HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok().body("User registered successfully!");
	}
}
