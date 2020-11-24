package com.morelang.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.morelang.dto.History;
import com.morelang.dto.HistoryVideo;
import com.morelang.dto.Member;
import com.morelang.dto.pointCharge;
import com.morelang.dto.recommendChannel;
import com.morelang.repository.ChargeRepository;
import com.morelang.repository.HistoryRepository;
import com.morelang.repository.HistoryVideoRepository;
import com.morelang.repository.MemberRepository;
import com.morelang.repository.RecommendRepository;

@Service
public class HistoryServiceImpl implements HistoryService{
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private HistoryVideoRepository historyVideoRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ChargeRepository chargeRepository;
	
	@Autowired
	private RecommendRepository recommendRepository;
	private static int POINT_VALUE = 100;

	@Override
	public String watchVideo(String accessToken, HistoryVideo watched) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Optional<HistoryVideo> video = historyVideoRepository.findByYoutubeVideoid(watched.getYoutubeVideoid());
			boolean is_watch = false;
			HistoryVideo info;
			// 누군가 봐서 비디오에 값이 들어가면?
			if(video.isPresent()) {
				Optional<History> history = historyRepository.findByMember_idAndVideo_vid(m.get().getId(),video.get().getVid());
				//내가 본 기록이 있으면?
				if(history.isPresent()) {
					is_watch = true;
					info = video.get();
					historyVideoRepository.save(info);
					return "success";
				}
			}
			if(!is_watch && m.get().getPoint()>=POINT_VALUE) {
				if(video.isPresent()) {
					info = video.get();
					historyVideoRepository.save(info);
				}else {
					info = watched;
					info.setCount(1);
					historyVideoRepository.save(info);
				}
				pointCharge pc = new pointCharge();
				pc.setCharge(false);
				pc.setChargeAmount(100);
				pc.setMember(m.get());
				History h = new History();
				h.setMember(m.get());
				h.setVideo(info);
				m.get().setPoint(m.get().getPoint()-POINT_VALUE);
				pc.setRemainPoint(m.get().getPoint());
				memberRepository.save(m.get());
				chargeRepository.save(pc);
				historyRepository.save(h);
				return "success";
			}else {
				return "잔액부족";
			}
			

		}else {
			return "fail";
		}
	}

	@Override
	public List<HistoryVideo> myVideoList(String accessToken,Pageable pageable){
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			List<History> history = historyRepository.findByMember_id(m.get().getId(),pageable);
			List<Integer> viewId = new ArrayList<>();
			System.out.println(history.size());
			for(int i=0; i<history.size(); i++) {
				viewId.add(Integer.valueOf(history.get(i).getVideo().getVid()));
			}
			 return historyVideoRepository.findByVidIn(viewId);
		}
		return null;
	}
	
	@Override
	public Integer myVideoListSize(String accessToken){
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			List<History> history = historyRepository.findByMember_id(m.get().getId());
			List<Integer> viewId = new ArrayList<>();
			System.out.println(history.size());
			for(int i=0; i<history.size(); i++) {
				viewId.add(Integer.valueOf(history.get(i).getVideo().getVid()));
			}
			 return historyVideoRepository.findByVidIn(viewId).size();
		}
		return null;
	}
	
	@Override
	public Boolean is_view(String accessToken,HistoryVideo watched) {
		Optional<Member> m = memberRepository.findByAccessToken(accessToken);
		if(m.isPresent()) {
			Optional<HistoryVideo> history_video =  historyVideoRepository.findByYoutubeVideoid(watched.getYoutubeVideoid());
			if(history_video.isPresent()) {
				HistoryVideo hvideo = history_video.get();
				Optional<History> history= historyRepository.findByMember_idAndVideo_vid(m.get().getId(), history_video.get().getVid());
				hvideo.setCount(hvideo.getCount()+1);
				historyVideoRepository.save(hvideo);
				if(history.isPresent()) {
					return true;
				}else {
					return false;
				}
			}else {
				watched.setCount(1);
				historyVideoRepository.save(watched);
				return false;
			}
		}else {
			return null;
		}
	}
	
	@Override
	public List<recommendChannel> recommendList(String country){
		List<recommendChannel> list = null;
		if(country == null) {
			list = recommendRepository.findAll();
		}else {
			list = recommendRepository.findByCountry(country);
		}
		Collections.shuffle(list);
		int size = list.size()>=10? 10: list.size();
		List<recommendChannel> subList = new ArrayList<>();
		for(int i=0; i<size; i++) {
			subList.add(list.get(i));
		}
		return subList;
	}
	
	@Override
	public Page<HistoryVideo> highCountVideo(String Defaultlang, Pageable pageable) {
		if(Defaultlang == null) {
			return historyVideoRepository.findAll(pageable);
		} else {
			if (Defaultlang.equals("jp"))
				Defaultlang = "ja";
			else if (Defaultlang.equals("cn"))
				Defaultlang = "zh";
			return historyVideoRepository.findByDefaultLanguage(Defaultlang, pageable);
		}
	}
}
