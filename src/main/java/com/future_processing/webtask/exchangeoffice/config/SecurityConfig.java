package com.future_processing.webtask.exchangeoffice.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .withDefaultSchema()
            .usersByUsernameQuery("select username, password from users where username = ?");
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		    .authorizeRequests()
		        .antMatchers("/resources/**").permitAll()
		        .antMatchers("/register").permitAll()
		        .anyRequest().authenticated()
		        .and()
			.formLogin()
			    .loginPage("/login")
			    .failureUrl("/login?error")
			    .usernameParameter("username")
			    .passwordParameter("password")
			    .permitAll()
			    .and()
			.logout()
			    .logoutSuccessUrl("/login?logout")
			    .permitAll();
	}
	
}
