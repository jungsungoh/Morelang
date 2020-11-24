package com.morelang.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.morelang.dto.Member;
import com.morelang.dto.Voca;
import com.morelang.dto.VocaQuizLog;
import com.morelang.dto.VocaQuizLogSub;
import com.morelang.dto.VocaSub;
import com.morelang.repository.MemberRepository;
import com.morelang.repository.VocaQuizRepository;
import com.morelang.repository.VocaRepository;

@Service
public class VocaServiceImpl implements VocaService{
	@Autowired
	VocaRepository vocaRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	VocaQuizRepository vocaQuizRepository;
	
	@Override
	public String registVoca(String accessToken,Voca input) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Member member = m.get();
			Voca s = input;
			s.setMember(member);
			vocaRepository.save(s);
			return "success";
		}
		return "fail";
	}
	
	@Override
	public String DeleteVoca(String accessToken, Long vocaId) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Optional<Voca> voca = vocaRepository.findByMember_idAndVocaId(m.get().getId(), vocaId);
			if(voca.isPresent()) {
				vocaRepository.delete(voca.get());
				return "success";
			}
		}
		return "fail";
	}
	
	@Override
	public Page<VocaSub> MyVoca(String accessToken,Pageable pageable,String[] country) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Page<VocaSub> voca;
			if(country == null || country.length == 0) {
				voca = vocaRepository.findByMember_id(m.get().getId(), pageable);
			}else {
				voca = vocaRepository.findByMember_idAndCountryIn(m.get().getId(),country, pageable);
			}
			return voca;
		}else {
			return null;
		}
	}
	
	@Override
	public String updateVoca(String accessToken, Voca updateVoca) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Optional<Voca> voca = vocaRepository.findByMember_idAndVocaId(m.get().getId(), (long)(updateVoca.getVocaId()));
			if(voca.isPresent()) {
				vocaRepository.save(updateVoca);
				return "success";
			}
		}
		return "fail";
	}
	@Override
	public String changeMean(String accessToken, Integer vocaId, String mean) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Optional<Voca> voca = vocaRepository.findByMember_idAndVocaId(m.get().getId(), Long.valueOf(vocaId));
			if(voca.isPresent()) {
				voca.get().setEachMean(mean);
				vocaRepository.save(voca.get());
				return "success";
			}
		}
		return "fail";
	}
	@Override
	public List<String> myVocaCountry(String accessToken) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		List<String> country = new ArrayList<>();
		if(m.isPresent()) {
			country = vocaRepository.findDistinctCountry(m.get().getId());
			return country;
		}else {
			return null;
		}
	}
	@Override
	public String makeLearn(String accessToken, Long VocaId) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Optional<Voca> voca = vocaRepository.findByMember_idAndVocaId(m.get().getId(), Long.valueOf(VocaId));
			if(voca.isPresent()) {
				Voca v1 = voca.get();
				if(v1.isLearn()) {
					v1.setLearn(false);
				}else {
					v1.setLearn(true);
				}
				vocaRepository.save(v1);
				return "success";
			}
		}
		return "fail";
	}
	@Override
	public Map<String,Object> vocaQuize(String accessToken, String country, Integer idx) throws IOException {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		Map<String, Object> map = new HashMap<>();
		List<Voca> vocaList = null;
		List<Voca> meanList = null;
		List<VocaSub> sizeList = null;
		if(m.isPresent()) {
			if(country == null) {
				vocaList = vocaRepository.findByMember_idAndUsed(m.get().getId(), false);
				sizeList = vocaRepository.findByMember_id(m.get().getId());
			}else {
				vocaList = vocaRepository.findByMember_idAndUsedAndCountry(m.get().getId(), false, country);
				sizeList = vocaRepository.findByMember_idAndCountry(m.get().getId(),country);
			}
			Integer maxIdx = sizeList.size() >=10?10:sizeList.size();
			if(vocaList.size() == 0) {
				map.put("result", "not exist");
				return map;
			}else {
				Map<String, Object> ma = new HashMap<>();
				List<String> answer_list = new ArrayList<>();
				ma.put("num", idx);
				ma.put("maxIdx", maxIdx-1);
				Collections.shuffle(vocaList);
				Voca v1 = vocaList.get(0);
				ma.put("problem", v1.getEachVoca());
				String answer = vocaMean(v1.getEachMean());
				answer_list.add(answer);
				meanList = vocaRepository.distinctMean(v1.getEachMean());
				Collections.shuffle(meanList);
				for(int i=0; i<3;i++) {
					answer_list.add(meanList.get(i).getEachMean());
				}
				Collections.shuffle(answer_list);
				ma.put("answer", answer_list.indexOf(answer));
				ma.put("answer_list", answer_list);
				map.put("result", ma);
				v1.setUsed(true);
				vocaRepository.save(v1);
				return map;
			}
		}else {
			map.put("result", "fail");
			return map;
		}
		
	}
	
	public String vocaMean(String voca) throws IOException {
		String url = "https://dic.daum.net/search.do?q="+voca;
		Connection conn = Jsoup.connect(url);
		Document html = conn.get();
		Element element = html.body();
		Elements el = element.select(".txt_search");
		String element1 = el.get(0).text();
		System.out.println(element1);
		return element1;
	}

	@Override
	public String initQuiz(String accessToken) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			vocaRepository.updateStatus(m.get().getId());
			return "success";
		}else {
			return "fail";
		}
	}
	@Override
	public String QuizResult(String accessToken, String country, Integer answer_cnt, Integer all_cnt) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			VocaQuizLog logs = new VocaQuizLog();
			Double acc = (double)answer_cnt/(all_cnt);
			logs.setAnswerCnt(answer_cnt);
			logs.setWrongCnt(all_cnt-answer_cnt);
			logs.setMember(m.get());
			logs.setAccRate(Math.round(acc*1000)/10.0);
			logs.setAllCnt(all_cnt);
			if(country == null) {
				logs.setCountry("all");
			}else {
				logs.setCountry(country);
			}
			vocaQuizRepository.save(logs);
			return "success";
		}else {
			return "fail";
		}
	}
	@Override
	public Map<String,Object> QuizLogData(String accessToken){
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Map<String, Object> map = new HashMap<>();
			List<VocaQuizLogSub> list = new ArrayList<>();
			List<String> alist = vocaQuizRepository.findDistinctCountry(m.get().getId());
			map.put("All", vocaQuizRepository.findByMember_idAndCountry(m.get().getId(),"All",(Sort.by(Sort.Direction.ASC, "quizTime"))));
			for(int i=0; i<alist.size(); i++) {
				list = vocaQuizRepository.findByMember_idAndCountry(m.get().getId(),alist.get(i),(Sort.by(Sort.Direction.ASC, "quizTime")));
				map.put(alist.get(i), list);
			}
			return map;
		}else {
			return null;
		}
	}
}
