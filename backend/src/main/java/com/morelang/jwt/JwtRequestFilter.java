package com.morelang.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.morelang.repository.MemberRepository;

import io.jsonwebtoken.ExpiredJwtException;



@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	JwtTokenUtil jtu;


	public Authentication getAuthentication(String token) {
		Map<String, Object> parseInfo = jtu.getUserParseInfo(token);
		System.out.println("parseinfo: " + parseInfo);
		List<String> rs =(List)parseInfo.get("role");
		Collection<GrantedAuthority> tmp= new ArrayList<>();
		for (String a: rs) {
			tmp.add(new SimpleGrantedAuthority(a));
		}
		UserDetails userDetails = User.builder().username(String.valueOf(parseInfo.get("username"))).authorities(tmp).password("asd").build();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		return usernamePasswordAuthenticationToken;
	}

	@Bean
	public FilterRegistrationBean JwtRequestFilterRegistration (JwtRequestFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setEnabled(false);
		return registration;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException{

		System.out.println("REQUEST : " + request.getHeader("Authorization"));
		String requestTokenHeader = request.getHeader("Authorization");
		String refreshToken = request.getHeader("refreshToken");

		logger.info("tokenHeader: " + requestTokenHeader);
		String username = null;
		String jwtToken = null;
		String jwtToken2 = null;
		if(refreshToken != null && refreshToken.startsWith("Bearer ")) {
			System.out.println("dd!!!");
			jwtToken2 = refreshToken.substring(7);
			//jwtToken = requestTokenHeader.substring(7);
			response.setHeader("jwtToken2", jwtToken2);
			chain.doFilter(request, response);
		}else {
			// Bearer를 앞에 쓰는 이유눈 업계 표준, 함부로 바꿔서 사용하면 안된다. ex) ssalog로 바꿔서 쓰려고 했는데 클날뻔...ㅎㅎ
			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				logger.info("token in requestfilter: " + jwtToken);

				try {
					username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				} catch (IllegalArgumentException e) {
					logger.warn("Unable to get JWT Token");
				}
				catch (ExpiredJwtException e) {
					username = e.getClaims().getSubject();  
					logger.info("[access token이 만료된 사용자 이름123] " + username);
					response.setHeader("error", "expired");
				}
			} else {
				logger.warn("JWT Token does not begin with Bearer String");
			}
			if (username == null) {
				logger.info("token maybe expired: username is null.");
			}else if (!memberRepository.findByAccessToken(jwtToken).isPresent()) {
				logger.warn("this token already logout!");
			} else {
				//DB access 대신에 파싱한 정보로 유저 만들기!
				Authentication authen =  getAuthentication(jwtToken);
				//만든 authentication 객체로 매번 인증받기
				SecurityContextHolder.getContext().setAuthentication(authen);
				response.setHeader("accessToken", jwtToken);
				response.setHeader("username", username);
			}
			chain.doFilter(request, response);
		}
	}
}
