package com.morelang.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.morelang.dto.Member;
import com.morelang.dto.Scrap;
import com.morelang.dto.ScrapSub;
import com.morelang.repository.MemberRepository;
import com.morelang.repository.ScrapRepository;

@Service
public class ScrapServiceImpl implements ScrapService{
	@Autowired
	ScrapRepository scrapRepository;
	@Autowired
	MemberRepository memberRepository;
	
	@Override
	public String DoScrap(String accessToken,Scrap input) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Member member = m.get();
			Scrap s = input;
			s.setMember(member);
			scrapRepository.save(s);
			return "true";
		}
		return "false";
	}
	
	@Override
	public String DeleteScrap(String accessToken, Integer scrapId) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Optional<Scrap> s = scrapRepository.findByMember_idAndScrapId(m.get().getId(), scrapId);
			if(s.isPresent()) {
				scrapRepository.delete(s.get());
				return "true";
			}
		}
		return "false";
	}
	
	@Override
	public Page<ScrapSub> MyScrap(String accessToken,Pageable pageable) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Page<ScrapSub> s = scrapRepository.findByMember_id(m.get().getId(),pageable);
			return s;
		}else {
			return null;
		}
	}
	
	@Override
	public String updateScrap(String accessToken, Scrap updateScrap) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Optional<Scrap> s = scrapRepository.findByMember_idAndScrapId(m.get().getId(), (updateScrap.getScrapId()));
			if(s.isPresent()) {
				scrapRepository.save(updateScrap);
				return "true";
			}
		}
		return "false";
	}
}
