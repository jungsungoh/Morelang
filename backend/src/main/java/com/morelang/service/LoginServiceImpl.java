package com.morelang.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.morelang.dto.Member;
import com.morelang.dto.pointCharge;
import com.morelang.jwt.JwtTokenUtil;
import com.morelang.repository.ChargeRepository;
import com.morelang.repository.MemberRepository;
import com.morelang.util.Mail;

import io.swagger.annotations.ApiOperation;

@Service
public class LoginServiceImpl implements LoginService{
	private Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	private PasswordEncoder bcryptEncoder;
	@Autowired
	private JwtUserDetailsService userDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private ChargeRepository chargeRepository;
	@Override
	public Map<String, Object> Regist (Member member,String providerName) {
		String userid = member.getUserid();
		Map<String, Object> map = new HashMap<>();
		System.out.println(memberRepository.findByUserid(member.getUserid()));
		// 아이디 중복체크 진행
		if (!memberRepository.findByUserid(userid).isPresent()) {
			member.setUserid(userid);
			if (userid.equals("admin")) {
				member.setRole("ROLE_ADMIN");
			} else {
				member.setRole("ROLE_USER");
			}
			member.setName(member.getName());
			member.setPassword(bcryptEncoder.encode(member.getPassword()));	
			member.setProviderName(providerName);
			member.setPoint(1000);
			memberRepository.save(member); 
			Optional<Member> m2 = memberRepository.findByUserid(member.getUserid());
			pointCharge pc = new pointCharge();
			pc.setCharge(true);
			pc.setChargeAmount(1000);
			pc.setMember(m2.get());
			pc.setRemainPoint(m2.get().getPoint());
			chargeRepository.save(pc);
			map.put("success", true); 
		} else {
			map.put("success", false);
			map.put("message", "duplicated username");
		}
		return map;
	}
	
	@Override
	public Map<String, Object> login(String Userid, String password) {
		try {
			// 현재 사용자가 입력한 username과 password가 내 DB의 정보와 일치하는가?
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(Userid, password));
		} catch (Exception e){
			System.out.println("비정상적인 요청입니다.");
			throw e;
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(Userid);
		final String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
		final String refreshToken = jwtTokenUtil.generateRefreshToken(Userid);

		Optional<Member> m2 = memberRepository.findByUserid(Userid);
		Member m = m2.get();
		m.setAccessToken(accessToken);
		m.setRefreshToken(refreshToken);

		String nickname = m.getName();
		logger.info("generated access token: " + accessToken);
		logger.info("generated refresh token: " + refreshToken);
		
		memberRepository.save(m);
		Map<String, Object> map = new HashMap<>();
		map.put("accessToken", accessToken);
		map.put("refreshToken", refreshToken);
		map.put("nickname", nickname);
		map.put("userid", m.getUserid());
		map.put("memberid", m.getId());
		map.put("point", m.getPoint());
		return map;
	}
	
	@Override
	public String logout(String userid) {
		try {
			Optional<Member> member = memberRepository.findByUserid(userid);
			Member m = member.get();
			m.setAccessToken(null);
			m.setRefreshToken(null);
			return "success";
		} catch (IllegalArgumentException e) {
			logger.warn("[ERROR] 사용자가 존재하지 않습니다!");
		}
		return "fail";
	}
	@Override
	public boolean checkId(String userid) {
		if (!memberRepository.findByUserid(userid).isPresent()) return true;
		else return false;
	}
	@Override
	public boolean checkNickname(String name) {
		if (memberRepository.findByName(name) == null) return true;
		else return false;
	}
	@Override
	public String get_profile_img(String username){
		Optional<Member> m = memberRepository.findByUserid(username);
		Member member = m.get();
		if(member.getProfileImg() == null){
			return "default";
		}else {
			return member.getProfileImg();
		}
	}
	@Override
	public Map<String,Object> Googlelogin(String authCode, String redirect) throws IOException {
		GoogleTokenResponse tokenResponse =
				new GoogleAuthorizationCodeTokenRequest(
						new NetHttpTransport(),
						JacksonFactory.getDefaultInstance(),
						"https://oauth2.googleapis.com/token",
						"258439612277-a2k3f6ro1jvdkbois85pt4cngrs6hctk.apps.googleusercontent.com",
						"H56LQIIC1wfc6_8x7WWhrXV3",
						authCode,
						redirect.substring(0,redirect.indexOf("Login")-1)).execute();
//		String accessToken = tokenResponse.getAccessToken();
//		String refreshToken = tokenResponse.getRefreshToken();
		GoogleIdToken idToken = tokenResponse.parseIdToken();
		GoogleIdToken.Payload payload = idToken.getPayload();
		String userId = payload.getSubject();  // Use this value as a key to identify a user.
		String email = payload.getEmail();
		String name = (String) payload.get("name");
		String pictureUrl = (String) payload.get("picture");
		Optional<Member> m = memberRepository.findByUserid(userId);
		Member m1 = null;
		if (m.isPresent()) {
			m1 = m.get();
			m1.setName(name);
			m1.setEmail(email);
			m1.setProfileImg(pictureUrl);
		} else {
			m1 = new Member();
			m1.setUserid(userId);
			m1.setName(name);
			m1.setProfileImg(pictureUrl);
			m1.setProviderName("Google");
			m1.setEmail(email);
			m1.setPoint(0);
			m1.setRole("ROLE_USER");
			m1.setPassword(name);
			Regist(m1,"Google");
		}
		return login(m1.getUserid(),name);
	}
	//GET/POST /v2/user/me HTTP/1.1
	// Host: kapi.kakao.com
	// Authorization: Bearer {ACCESS_TOKEN}
	// Content-type: application/x-www-form-urlencoded;charset=utf-8
	@Override
	public Map<String, Object> KakaoLogin(String accessToken) {
		String reqURL = "https://kapi.kakao.com/v2/user/me";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				String result = "";
				while ((line = br.readLine()) != null) {
					result += line;
				}
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(result);
				JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
				JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
				String userid = element.getAsJsonObject().get("id").getAsString();
				String nickname = properties.getAsJsonObject().get("nickname").getAsString();
				JsonElement nprofile_image = properties.getAsJsonObject().get("profile_image");
				JsonElement nemail = kakao_account.getAsJsonObject().get("email");
				String email = nemail == null ? null : nemail.getAsString();
				String profile_image = nprofile_image == null
						? "https://file3.instiz.net/data/cached_img/upload/2020/02/26/12/f7975c2dacddf8bf521e7e6d7e4c02ee.jpg"
						: nprofile_image.getAsString();
				Optional<Member> m = memberRepository.findByUserid(userid);
				Member m1 = null;
				if (m.isPresent()) {
					m1 = m.get();
					m1.setName(nickname);
					m1.setEmail(email);
					m1.setProfileImg(profile_image);
				} else {
					m1 = new Member();
					m1.setUserid(userid);
					m1.setName(nickname);
					m1.setProfileImg(profile_image);
					m1.setProviderName("Kakao");
					m1.setEmail(email);
					m1.setPoint(0);
					m1.setRole("ROLE_USER");
					m1.setPassword(nickname);
					Regist(m1,"KAKAO");
				}
				return login(m1.getUserid(),nickname);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//GET v1/nid/me HTTP/1.1
	//Host: openapi.naver.com
	//User-Agent: curl/7.43.0
	//Accept: */*
	//Content-Type: application/xml
	//Authorization: Bearer {네이버 아이디로 로그인 인증 후 받은 접근 토큰 값}
	// https://nid.naver.com/oauth2.0/token?client_id={클라이언트 아이디}&client_secret={클라이언트 시크릿}&grant_type=authorization_code&state={상태 토큰}&code={인증 코드}
	@Override
	public Map<String,Object> NaverLogin(String code) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000); // 읽기시간초과, ms factory.setConnectTimeout(3000); // 연결시간초과, ms 
        HttpClient httpClient = HttpClientBuilder.create() 
        		.setMaxConnTotal(100) // connection pool 적용 
        		.setMaxConnPerRoute(5) // connection pool 적용 
        		.build(); 
        factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅
        RestTemplate restTemplate = new RestTemplate(factory);
        String path = "https://nid.naver.com/oauth2.0/token?client_id={client_id}&client_secret={client_secret}&grant_type=authorization_code&state={state}&code={code}";
        final Map<String, String> variables = new HashMap<>();
        variables.put("client_id","YOwQRknl_3ldIN3E5UQN");
        variables.put("client_secret","rAl2UNkU28");
        variables.put("state", "234235");
        variables.put("redirect", "false");
        variables.put("code", code);
        ResponseEntity<Map> response = restTemplate.getForEntity(path, Map.class, variables);
        System.out.println(response);
        String naverAccessToken = String.valueOf(response.getBody().get("access_token"));
		path = "https://openapi.naver.com/v1/nid/me";
		final HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "curl/7.43.0");
        headers.set("Authorization", "Bearer " + naverAccessToken);
        final HttpEntity<String> entity = new HttpEntity<String>(headers);
        response = restTemplate.exchange(path, HttpMethod.GET, entity, Map.class); 
        System.out.println(response);
        Map map = (Map)response.getBody().get("response");
        String userid = String.valueOf(map.get("id"));
        String name = String.valueOf(map.get("nickname"));
        String pictureUrl = String.valueOf(map.get("profile_image"));
        String email = String.valueOf(map.get("email"));
        Optional<Member> m = memberRepository.findByUserid(userid)                                                                                                                                                                                                                                                                                                                                                                                                      ;
		Member m1 = null;
		if (m.isPresent()) {
			m1 = m.get();
			m1.setName(name);
			m1.setEmail(email);
			m1.setProfileImg(pictureUrl);
		} else {
			m1 = new Member();
			m1.setUserid(userid);
			m1.setName(name);
			m1.setProfileImg(pictureUrl);
			m1.setProviderName("Naver");
			m1.setEmail(email);
			m1.setPoint(0);
			m1.setRole("ROLE_USER");
			m1.setPassword(name);
			Regist(m1,"Naver");
		}
		return login(m1.getUserid(),name);
	}
	@Override
	public Boolean find_pw(String username) {
		Optional<Member> m2 = memberRepository.findByUserid(username);
		Member member = m2.get();
		if(member != null && member.getProviderName().equals("Our")) {
			Mail m = Mail.getInstance();
	    	String ran = m.sendMail(username,2);
	    	member.setPassword(bcryptEncoder.encode(ran));
	    	memberRepository.save(member);
			return true;
		}
		return false;
	}

}
