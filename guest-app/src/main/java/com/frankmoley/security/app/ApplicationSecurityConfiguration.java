package com.frankmoley.security.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import com.frankmoley.security.app.auth.UserDetailService;

@Configuration
@EnableWebSecurity
//To turn on our method level security on 
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//By using http builder we will set up our authentication
		http
		.csrf().disable()//This will disable CSRF support
		.authorizeRequests()
		//Now we will add the patterns for which no authentication is required
		.antMatchers("/","/index","/css/*","/js/*").permitAll() 
		//For any other URL pattern it should needed to be authenticated
		.anyRequest().authenticated()
		.and()
		//We set up authentication type to httpBasic by default it was basic http based
		//.httpBasic();
		//We set up authentication type to Form based
		.formLogin()
		.loginPage("/login").permitAll()
		.and()
		.logout().invalidateHttpSession(true)
		.clearAuthentication(true)
		.logoutRequestMatcher((new AntPathRequestMatcher("/logout")))
		.logoutSuccessUrl("/logout-success").permitAll();
	}

	/*Commenting it for getting user from DB
	
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		// TODO Auto-generated method stub
		List<UserDetails> users = new ArrayList<UserDetails>();
		users.add(User.withDefaultPasswordEncoder().
				username("avicse007")
				.password("password")
				.roles("USER","ADMIN")
				.build());
		users.add(User.withDefaultPasswordEncoder().
				username("avicse006")
				.password("password")
				.roles("USER")
				.build());
		
		return new InMemoryUserDetailsManager(users);
	}
	
	*/
	
	@Autowired
	private UserDetailService userDetailService;
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService);
		//Do not use NoOpPasswordEncoder in production code 
		//provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		//Lets use Bcrypt for password encodder 
		provider.setPasswordEncoder(new BCryptPasswordEncoder(11));
		provider.setAuthoritiesMapper(authoritiesMapper());
		return provider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Bean
	GrantedAuthoritiesMapper authoritiesMapper() {
		SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
		authorityMapper.setConvertToUpperCase(true);
		authorityMapper.setDefaultAuthority("USER");
		return authorityMapper;
	}
	
	

}
