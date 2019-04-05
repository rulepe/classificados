package projeto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import projeto.services.Servicos;

@EnableWebSecurity
@ComponentScan(basePackageClasses = AppUserDetailService.class)
public class SpringSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		 auth
		 	.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.authorizeRequests()
			.antMatchers("/css/**", "/js/**", "/images/**").permitAll()
			.antMatchers("/").permitAll()
			.antMatchers("/cadastre-se").permitAll()
			.antMatchers("/cadastrar").permitAll()
			.antMatchers("**/fotos/").permitAll()
			.antMatchers("/produto/**").permitAll()
			.antMatchers("/pesquisa/**").permitAll()
			.antMatchers("/recuperarsenha/**").permitAll()
			.antMatchers("/recuperar/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login").permitAll()
			.and()
				.logout().permitAll()
			.and()
				.formLogin().failureUrl("/loginError").permitAll();		
		
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean

	public Servicos servicos() {
		return new Servicos();
	}
}
