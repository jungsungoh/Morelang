package com.morelang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.morelang.jwt.JwtAuthenticationEntryPoint;
import com.morelang.jwt.JwtRequestFilter;






@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	@Autowired
	private UserDetailsService jwtUserDetailsService;
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	// Swagger 관련 문서는 모두가 볼 수 있도록 설정하기 위해 WebSecurity ignore 설정 진행
	@Override 	
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**", "/newuser/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
		httpBasic().disable(). // 기본 제공해주는 로그인 페이지 사용 안함 (우리가 만든 로그인 페이지로 커스텀하기 위해서)
		cors().and().		   
		csrf().disable().	   // rest api 이므로, 페이지 구현을 안하므로, csrf 보안 사용 안함
		sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS). // 우린 토큰 인증이므로 session 사용 안함
		and().
			authorizeRequests().	// 페이지별 접근 가능한 권한 설정
			antMatchers("/newuser/**", "/swagger-ui.html").permitAll().
		and().
			authorizeRequests().	// 페이지별 접근 가능한 권한 설정
			antMatchers("/user/**").hasAnyRole("USER", "ADMIN").
		and().
			authorizeRequests().
			anyRequest().
			authenticated().
		and().
			exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).
		and().
			addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}


	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
