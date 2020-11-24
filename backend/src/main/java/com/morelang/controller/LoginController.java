package com.morelang.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morelang.dto.Member;
import com.morelang.service.LoginService;
import com.morelang.util.Mail;
import io.swagger.annotations.ApiOperation;



//import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
//@RequestMapping("/login")
public class LoginController {
	@Autowired
	private LoginService loginService;

	@ApiOperation(value = "[회원가입 기능] 신규회원을 등록한다.")
	@PostMapping(path="/newuser/add") 
	public ResponseEntity<Map<String, Object>> Regist (@RequestBody Member member) {
		return new ResponseEntity<Map<String, Object>>(loginService.Regist(member,"Our"),HttpStatus.OK);
	}
	@ApiOperation(value = "[회원가입 기능] 중복체크 이걸로 하면 될듯? 중복되는 아이디가 DB에 없는지 확인(check)한다.")
	@GetMapping(path="/newuser/checkid")
	public ResponseEntity<Boolean> checkId (@RequestParam("username") String username) {
		return new ResponseEntity<Boolean>(loginService.checkId(username), HttpStatus.OK);
	}

	@ApiOperation(value = "[회원가입 기능](p-011_회원가입) 중복되는 닉네임이 DB에 없는지 확인(check)한다. 사용 가능하면 true,불가능하면 false")
	@GetMapping(path="/newuser/checkNickname")
	public ResponseEntity<Boolean> checkNickname (@RequestParam("nickname") String name) {
		return new ResponseEntity<Boolean>(loginService.checkNickname(name), HttpStatus.OK);
	}

	@ApiOperation(value = "[로그인 기능](p-012_로그인) 회원로그인을 진행한다.")
	@PostMapping(path = "/newuser/login")
	public ResponseEntity<Map<String, Object>> login(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
		return new ResponseEntity<Map<String, Object>>(loginService.login(username, password), HttpStatus.OK);
	}

	@ApiOperation(value = "[logout 기능](p-012_로그인) 로그아웃을 진행한다.")
	@PostMapping(path="/user/out")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		String username = response.getHeader("username");
		return new ResponseEntity<String>(loginService.logout(username),HttpStatus.OK);
	}
//	@ApiOperation(value = "[비밀번호 변경] 비밀번호 변경하는 api")
//	@PutMapping(path="/user/change_pw")
//	public ResponseEntity<Boolean> change_pw(HttpServletResponse response, @RequestParam("password") String password) {
//		String username = response.getHeader("username");
//		return new ResponseEntity<Boolean>(accountService.change_pw(username, password), HttpStatus.OK);
//	}
	@ApiOperation(value = "[비밀번호 찾기] 아이디를 입력해서 해당 이메일로 변경된 비밀번호를  전송하는 api")
	@GetMapping(path="/newuser/find_pw")
	public ResponseEntity<Boolean> find_pw(@RequestParam("username") String username) {
		return new ResponseEntity<Boolean>(loginService.find_pw(username), HttpStatus.OK);
	}

//	@ApiOperation(value = "[Token refresh 기능] 클라이언트가 받은 refresh token을 이용해, db에 존재하는 값과 일치하면, 신규 Token 갱신과정을 진행한다.")
//	@PostMapping(path="/newuser/refresh")
//	public ResponseEntity<Map<String, Object>> requestForNewAccessToken(HttpServletResponse response) {
//		String refreshTokenFromDb = null;
//		Map<String, Object> map = new HashMap<>();
//		String refreshToken =response.getHeader("jwtToken2");
//		System.out.println(refreshToken + " =================");
//		try {
//			if (refreshToken != null) { //refresh를 같이 보냈으면.
//				String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
//				try {
//					System.out.println("username입니다 !" + username);
//					ValueOperations<String, Object> vop = redisTemplate.opsForValue();
//					Token result = (Token) vop.get(username);
//					refreshTokenFromDb = result.getRefreshToken();
//					logger.info("rtfrom db: " + refreshTokenFromDb);
//				} catch (IllegalArgumentException e) {
//					logger.warn("illegal argument!!");
//				}
//				System.out.println();
//				//둘이 일치하고 만료도 안됐으면 재발급 해주기.
//				if (refreshToken.equals(refreshTokenFromDb) && !jwtTokenUtil.isTokenExpired(refreshToken)) {
//					System.out.println("보내줍니다 !");
//					final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//					String newtok =  jwtTokenUtil.generateAccessToken(userDetails);
//					map.put("success", true);
//					map.put("accessToken", newtok);
//					System.out.println("accessToken = " + newtok);
//				} else {
//					map.put("success", false);
//					map.put("msg", "refresh token is expired.");
//				}
//			} else { //refresh token이 없으면
//				map.put("success", false);
//				map.put("msg", "your refresh token does not exist.");
//			}
//
//		} catch (Exception e) {
//			throw e;
//		}
//		return new ResponseEntity<Map<String, Object>>(map,HttpStatus.OK);
//	}
//	@ApiOperation(value = "[닉네임변경] Accesstoken으로 바꿀 유저를 조회하고 해당유저의 닉네임을 받은 닉네임으로 변경한다.")
//	@PutMapping(path="/user/change_nickname")
//	public ResponseEntity<String> change_nickname(HttpServletResponse response,@RequestParam("nickname") String nickname) {
//		String username = response.getHeader("username");
//		return new ResponseEntity<String>(accountService.change_nickname(nickname, username),HttpStatus.OK);
//	} 
	@PostMapping("/newuser/googlelogin")
	public ResponseEntity<?> googlelogin(@RequestParam("code") String code, @RequestParam("redirect") String redirect) throws IOException {
		return new ResponseEntity<Map<String,Object>>(loginService.Googlelogin(code, redirect),HttpStatus.OK);
	}
	@PostMapping("/newuser/kakaologin")
	public ResponseEntity<?> kakaologin(@RequestParam("accessToken") String accessToken) throws IOException {
		return new ResponseEntity<Map<String, Object>>(loginService.KakaoLogin(accessToken),HttpStatus.OK);
	}
	@PostMapping("/newuser/naverlogin")
	public ResponseEntity<?> naverlogin(@RequestParam("code") String code) throws IOException {
		return new ResponseEntity<Map<String, Object>>(loginService.NaverLogin(code),HttpStatus.OK);
	}
	@ApiOperation(value = "[이미지 경로 얻기] (p-014_마이페이지) 기능 : 프로필 사진을 얻을 수 있는 url을 준다.ex) http://localhost:8080/upload/resources/upload/{이 요청으로 얻은 값}")
	@GetMapping(path="/newuser/get_profile_img")
	public ResponseEntity<String> get_profile_img(@RequestParam("username") String username){
		return new ResponseEntity<String>(loginService.get_profile_img(username),HttpStatus.OK);
	}
	
	@ApiOperation(value = "[메일보내기] 이메일 인증을 만들어서,해당 이메일에 random값으로 메일을 보내고 그 random값을 return한다. 클라이언트단에서 비교해서 같으면 true, 틀리면 false 처리해주면 될듯.")
	@GetMapping(path="/newuser/check_email")
	public ResponseEntity<String> sendmail(@RequestParam("reciver") String email) {
		Mail m = Mail.getInstance();
		String ran = m.sendMail(email,1);
		return new ResponseEntity<String>(ran,HttpStatus.OK);
	} 
}
