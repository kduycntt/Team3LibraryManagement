package com.team3.LMS.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
 
@Configuration
// @EnableWebSecurity = @EnableWebMVCSecurity + Extra features
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
   @Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	   auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
   }
 
   @Override
   protected void configure(HttpSecurity http) throws Exception {
 
       http.csrf().disable();
  
       http.authorizeRequests().antMatchers("/index","/", "/home", "/login", "/logout", "/book").permitAll();
  
       http.authorizeRequests().antMatchers("/userInfo").access("hasAnyRole('MEMBER_USER', 'ADMIN')");
 
       // For ADMIN only
       http.authorizeRequests().antMatchers("/admin", "/admin/**").access("hasRole('ADMIN')");
 
       http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
 
       http.authorizeRequests().and().formLogin()//
               .loginPage("/login")
               .defaultSuccessUrl("/home")
               .failureUrl("/login?error=true")
               .usernameParameter("email")
               .passwordParameter("password")
               .and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");
 
   }
}