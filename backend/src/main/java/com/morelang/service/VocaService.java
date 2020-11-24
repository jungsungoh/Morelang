package com.morelang.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.morelang.dto.Voca;
import com.morelang.dto.VocaSub;

public interface VocaService {
	public String registVoca(String accessToken,Voca input);
	public String DeleteVoca(String accessToken, Long vocaId);
	public Page<VocaSub> MyVoca(String accessToken,Pageable pageable,String[] country);
	public String updateVoca(String accessToken, Voca updateVoca);
	public String makeLearn(String accessToken, Long VocaId);
	public List<String> myVocaCountry(String accessToken);
	public String changeMean(String accessToken, Integer vocaId, String mean);
	public Map<String,Object> vocaQuize(String accessToken, String country, Integer idx) throws IOException;
	public String vocaMean(String voca) throws IOException;
	public String initQuiz(String accessToken);
	public String QuizResult(String accessToken, String country,Integer answer_cnt, Integer all_cnt);
	public Map<String,Object> QuizLogData(String accessToken);
}
