package com.morelang.service;

import java.io.IOException;
import java.util.Map;

import com.morelang.dto.Member;


public interface LoginService {
	public Map<String, Object> Regist (Member member,String providerName);
	public Map<String, Object> login(String Userid, String password);
	public String logout(String userid);
	public boolean checkId(String userid);
	public boolean checkNickname(String name);
	public Map<String,Object> Googlelogin(String authCode, String redirect) throws IOException;
	public Map<String,Object> KakaoLogin(String accessToken);
	public Map<String,Object> NaverLogin(String code);
	public Boolean find_pw(String username);
	public String get_profile_img(String username);
}
