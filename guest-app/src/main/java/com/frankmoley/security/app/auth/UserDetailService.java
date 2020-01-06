package com.frankmoley.security.app.auth;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

	private final UserRepository userRepository;
	private final AuthGroupRepository authGroupRepository;
	
	public UserDetailService(UserRepository userRepository,AuthGroupRepository authGroupRepository) {
		this.userRepository = userRepository;
		this.authGroupRepository = authGroupRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = this.userRepository.findByUsername(username);
		if(user==null)
			throw new UsernameNotFoundException("User with username "+username+" is not found");
		List<AuthGroup> authGroups = authGroupRepository.findByUsername(username);
		return new PrincipalUser(user,authGroups);
	}

}
