package com.mkpolo.clienteapp;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//usamos el atributo http para indicar que sean permitidas todas las solicitudes de acuerdo al rol del usuario
		http.authorizeRequests()
		.antMatchers("/","/css/**","/js/**","/images/**").permitAll()
		.antMatchers("/views/clientes/").hasAnyRole("USER")
		.antMatchers("/views/clientes/").hasAnyRole("ADMIN")
		.antMatchers("/views/clientes/create").hasAnyRole("ADMIN")
		.antMatchers("/views/clientes/save").hasAnyRole("ADMIN")
		.antMatchers("/views/clientes/edit/**").hasAnyRole("ADMIN")
		.antMatchers("/views/clientes/delete/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and()
		.formLogin().permitAll()
		.and()
		.logout().permitAll();
	}


	@Autowired
	public void cofigurerSecurityGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.jdbcAuthentication()
		.dataSource(dataSource)
		.passwordEncoder(passEncoder)
		.usersByUsernameQuery("SELECT username, password, enable FROM users WHERE username=?")
		.authoritiesByUsernameQuery("SELECT u.username, r.rol FROM roles r INNER JOIN users u ON r.user_id=u.id WHERE u.username=?");
	}
}
