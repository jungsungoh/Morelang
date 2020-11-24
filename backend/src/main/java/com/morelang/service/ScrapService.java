package com.morelang.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.morelang.dto.Scrap;
import com.morelang.dto.ScrapSub;

public interface ScrapService {
	public String DoScrap(String accessToken,Scrap input);
	public String DeleteScrap(String accessToken, Integer scrapId);
	public Page<ScrapSub> MyScrap(String accessToken,Pageable pageable);
	public String updateScrap(String accessToken, Scrap updateScrap);
}
