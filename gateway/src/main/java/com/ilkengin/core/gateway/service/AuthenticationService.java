package com.ilkengin.core.gateway.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ilkengin.core.gateway.dto.SignUpRequest;
import com.ilkengin.core.gateway.error.EmailExistsException;
import com.ilkengin.core.gateway.error.UsernameExistsException;
import com.ilkengin.core.gateway.model.Role;
import com.ilkengin.core.gateway.model.RoleName;
import com.ilkengin.core.gateway.model.User;
import com.ilkengin.core.gateway.repository.RoleRepository;
import com.ilkengin.core.gateway.repository.UserRepository;

@Component
public class AuthenticationService {
	
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    public void createUser(SignUpRequest request) throws UsernameExistsException, EmailExistsException {
        if(isUsernameTaken(request.getUsername())) {
        	throw new UsernameExistsException();
        }
        
        if(isEmailTaken(request.getEmail())) {
        	throw new EmailExistsException();
        }
    	
    	// Creating user's account
        User user = new User(request.getName(), request.getUsername(),
        		request.getEmail(), encoder.encode(request.getPassword()));
 
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            roles.add(userRole);  
        
        user.setRoles(roles);
        userRepository.save(user);
    }
    
    private boolean isUsernameTaken(String username) {
    	return userRepository.existsByUsername(username);
    }
    
    
    private boolean isEmailTaken(String email) {
    	return userRepository.existsByEmail(email);
    }
}
