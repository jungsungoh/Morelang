package com.morelang.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.morelang.dto.HistoryVideo;
import com.morelang.dto.recommendChannel;

public interface HistoryService {
	public String watchVideo(String accessToken, HistoryVideo watched);
	public List<HistoryVideo> myVideoList(String accessToken,Pageable pageable);
	public Boolean is_view(String accessToken,HistoryVideo watched);
	public List<recommendChannel> recommendList(String country);
	public Page<HistoryVideo> highCountVideo(String Defaultlang, Pageable pageable);
	public Integer myVideoListSize(String accessToken);
}
