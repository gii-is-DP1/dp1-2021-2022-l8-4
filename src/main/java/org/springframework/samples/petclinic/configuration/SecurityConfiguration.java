package org.springframework.samples.petclinic.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/resources/**","/webjars/**","/h2-console/**").permitAll()
				.antMatchers(HttpMethod.GET, "/","/oups").permitAll()
				.antMatchers("/rules").permitAll()
				.antMatchers("/currentuser").permitAll()
				.antMatchers("/logout").authenticated()
				.antMatchers("/users/new").permitAll()
				.antMatchers("/users/**").hasAnyAuthority("admin")
				.antMatchers("/cards").permitAll()
				.antMatchers("/players/**/surrender").authenticated()
				.antMatchers("/players").hasAnyAuthority("admin")
				.antMatchers("/players/**/cards/discard").authenticated()
				.antMatchers("/players/**/cards/**/buy").authenticated()
				.antMatchers("/players/**/playerStatus").authenticated()
				.antMatchers("/players/**/playerStatus/**").authenticated()
				.antMatchers("/achievements").permitAll()
				.antMatchers("/cards/**").hasAnyAuthority("admin")
				.antMatchers("/players/**").hasAnyAuthority("admin")
				.antMatchers("/players/**/surrender").authenticated()
				.antMatchers("/games").hasAnyAuthority("admin")
				.antMatchers("/games/new").authenticated()
				.antMatchers("/games/lobbies").authenticated()
				.antMatchers("/games/**/start").authenticated()
				.antMatchers("/games/**/lobby").authenticated()
				.antMatchers("/games/**/playing").permitAll()
				.antMatchers("/games/**/exitTokyo").authenticated()
				.antMatchers("/games/**/finished").permitAll()
				.antMatchers("/achievements/**").hasAnyAuthority("admin")
				.antMatchers("/admin/**").hasAnyAuthority("admin")
				.anyRequest().denyAll()
				.and()
				 	.formLogin()
				 	/*.loginPage("/login")*/
				 	.failureUrl("/login-error")
				.and()
					.logout()
						.logoutSuccessUrl("/");
                // Configuración para que funcione la consola de administración 
                // de la BD H2 (deshabilitar las cabeceras de protección contra
                // ataques de tipo csrf y habilitar los framesets si su contenido
                // se sirve desde esta misma página.
                http.csrf().ignoringAntMatchers("/h2-console/**");
                http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
	      .dataSource(dataSource)
	      .usersByUsernameQuery(
	       "select username, password, enabled "
	        + "from users "
	        + "where username = ?")
	      .authoritiesByUsernameQuery(
	       "select userid, authority "
	        + "from authorities inner join users on authorities.userid = users.id "
	        + "where username = ?")	      	      
	      .passwordEncoder(passwordEncoder());	
	}
	

	@Bean
	public PasswordEncoder passwordEncoder() {	    
		PasswordEncoder encoder =  NoOpPasswordEncoder.getInstance();
	    return encoder;
	}
	
}


