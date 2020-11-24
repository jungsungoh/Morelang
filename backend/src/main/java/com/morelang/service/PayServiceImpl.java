package com.morelang.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.morelang.dto.Member;
import com.morelang.dto.PayApprove;
import com.morelang.dto.PayReady;
import com.morelang.dto.pointCharge;
import com.morelang.repository.ChargeRepository;
import com.morelang.repository.MemberRepository;

@Service
public class PayServiceImpl implements PayService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private ChargeRepository chargeRepository;
	
	@Override
	public PayReady ready(String item_name, String total_amount, String id) throws Exception {
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK fef33ec5acdbb638e3f4f80ad3f5a006");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("cid", "TC0ONETIME");
		body.add("partner_order_id", "order");
		body.add("partner_user_id", "user");
		body.add("item_name", item_name);
		body.add("quantity", "1");
		body.add("total_amount", total_amount);
		body.add("tax_free_amount", "0");
		if (id == null)
			body.add("approval_url", "https://morelang.gq/pay/approve");
		else
			body.add("approval_url", "https://morelang.gq/pay/approve?id=" + id);
		body.add("cancel_url", "https://morelang.gq/pay/fail");
		body.add("fail_url", "https://morelang.gq/pay/fail");
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
		PayReady payReady = template.postForObject("https://kapi.kakao.com/v1/payment/ready", entity, PayReady.class);
		payReady.setTotal_amount(total_amount);

		return payReady;
	}

	@Override
	public PayApprove approve(String accessToken, String tid, String pg_token, String total_amount) throws Exception {
		RestTemplate template = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK fef33ec5acdbb638e3f4f80ad3f5a006");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("cid", "TC0ONETIME");
		body.add("tid", tid);
		body.add("partner_order_id", "order");
		body.add("partner_user_id", "user");
		body.add("pg_token", pg_token);
		body.add("total_amount", total_amount);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
		PayApprove payApprove = template.postForObject("https://kapi.kakao.com/v1/payment/approve", entity,
				PayApprove.class);

		System.out.println(payApprove);
		chargePoint(accessToken,total_amount);
		return payApprove;
	}
	@Override
	public Integer MyPoint(String accessToken) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			return m.get().getPoint();
		}
		return null;
	}

	@Override
	public Page<pointCharge> PointLogs(String accessToken, Pageable pageable) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Page<pointCharge> pointlogs = chargeRepository.findByMember_id(m.get().getId(), pageable);
			return pointlogs;
		}else {
			return null;
		}
	}
	public void chargePoint(String accessToken, String mount) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		int amount = Integer.valueOf(mount)*10;
		if(m.isPresent()) {
			Member my = m.get();
			my.setPoint(my.getPoint()+amount);
			pointCharge pc = new pointCharge();
			pc.setRemainPoint(my.getPoint());
			pc.setMember(my);
			pc.setChargeAmount(amount);
			pc.setCharge(true);
			chargeRepository.save(pc);
		}
	}
	
	
}
