package com.morelang.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.language.v1beta2.PartOfSpeech.Tag;
import com.google.cloud.language.v1beta2.Token;
import com.morelang.dto.Learn;
import com.morelang.repository.LearnRepository;
import com.morelang.util.NLAnalyze;
@Service
public class LearnServiceImpl implements LearnService{
	
	@Autowired
	LearnRepository learnRepository;
	private String text = "Google, headquartered in Mountain View, unveiled the new Android phone at the Consumer Electronic Show. Sundar Pichai said in his keynote that users love their new Android phones.";
	//private String text2 = "だから みなさんに 僕の経験を共有したい";
	@Override
	public Map<String,Object> WordPuzzle(String input) {
		List<Token> LearnList =  NLAnalyze.getInstance().analyzeSyntax(input);//learnRepository.findAll();
		Map<Integer,String> complete = new HashMap<>();
		Map<Integer,Object> keyword = new HashMap<>();
		Map<Integer,String> sub_answer = new TreeMap<>();
		Map<Integer,String> answer = new HashMap<>();
		List<Token> subList = new ArrayList<>();
		List<String> subList2 = new ArrayList<>();
		int k = 1;
		for(Token learn : LearnList) {
			complete.put(k++,learn.getLemma());
			if(learn.getPartOfSpeech().getTag().equals(Tag.NOUN) || learn.getPartOfSpeech().getTag().equals(Tag.VERB) || learn.getPartOfSpeech().getTag().equals(Tag.ADJ)) {
				if(input.indexOf(learn.getLemma())!= -1 && !subList2.contains(learn.getLemma())) {
	        		subList.add(learn);
	        		subList2.add(learn.getLemma());
	        	}
	        }
		}
		Collections.shuffle(subList);
		int num = (int)Math.ceil((double)subList.size()/2);
		int time = 1;
		for(int i=0; i<num; i++) {
			sub_answer.put(subList2.indexOf(subList.get(i).getLemma()), subList.get(i).getLemma());
		}
		Map<String,Integer> searchMap = new HashMap<>();
		for(Integer key : sub_answer.keySet()) {
			answer.put(time, sub_answer.get(key));
			searchMap.put(sub_answer.get(key),time++);
		}
		for(int i=0; i<num; i++) {
				input = input.replaceFirst(subList.get(i).getLemma(), "ㅵ");
				Map<String, Object> temp_m = new HashMap<>();
				temp_m.put("original", searchMap.get(subList.get(i).getLemma()));
				temp_m.put("random", i+1);
				temp_m.put("key", subList.get(i).getLemma());
				//temp_m.put(subList.get(i).getLemma(), i+1);
				keyword.put(i+1,temp_m);
				System.out.printf("단어: %s\n", subList.get(i).getLemma());
				System.out.printf("품사: %s\n", subList.get(i).getPartOfSpeech().getTag());
		}
		
		StringTokenizer st = new StringTokenizer(input, "ㅵ", true);
		String[] resultArray = new String[st.countTokens()];
		int pas = 0;
		while(st.hasMoreTokens()) {
			String s = st.nextToken();
			if (s.equals("ㅵ"))
				s = "______";
			resultArray[pas++] = s;
		}
		Map<String,Object> m2 = new HashMap<>();
		m2.put("inputTextArray",resultArray);
		m2.put("quizeText", input);
		m2.put("keyword", keyword);
		m2.put("answer", answer);
		m2.put("count", num);
		return m2;
	}
	
	@Override
	public Map<String,Object> puzzeltest(String input) {
		List<Learn> LearnList =  learnRepository.findAll();
		Map<Integer,String> complete = new HashMap<>();
		Map<Integer,Object> keyword = new HashMap<>();
		Map<Integer,String> sub_answer = new TreeMap<>();
		Map<Integer,String> answer = new HashMap<>();
		List<Learn> subList = new ArrayList<>();
		List<String> subList2 = new ArrayList<>();
		int k = 1;
		String result = text;
		for(Learn learn : LearnList) {
			complete.put(k++,learn.getLemma());
			if(learn.getTag().equals("NOUN") || learn.getTag().equals("VERB") || learn.getTag().equals("ADJ")) {
	        	if(result.indexOf(learn.getLemma())!= -1 && !subList2.contains(learn.getLemma())) {
	        		subList.add(learn);
	        		subList2.add(learn.getLemma());
	        	}
	        }
		}
		
		Collections.shuffle(subList);
		int num = (int)Math.ceil((double)subList.size()/2);
		int time = 1;
		for(int i=0; i<num; i++) {
			sub_answer.put(subList2.indexOf(subList.get(i).getLemma()), subList.get(i).getLemma());
		}
		Map<String,Integer> searchMap = new HashMap<>();
		for(Integer key : sub_answer.keySet()) {
			answer.put(time, sub_answer.get(key));
			searchMap.put(sub_answer.get(key),time++);
		}
		for(int i=0; i<num; i++) {
				result = result.replaceFirst(subList.get(i).getLemma(), "______");
				Map<String, Object> temp_m = new HashMap<>();
				temp_m.put("original", searchMap.get(subList.get(i).getLemma()));
				temp_m.put("random", i+1);
				temp_m.put("key", subList.get(i).getLemma());
				//temp_m.put(subList.get(i).getLemma(), i+1);
				keyword.put(i+1,temp_m);
				System.out.printf("단어: %s\n", subList.get(i).getLemma());
				System.out.printf("품사: %s\n", subList.get(i).getTag());
		}
		
		StringTokenizer st = new StringTokenizer(result, " ", true);
		String[] resultArray = new String[st.countTokens()];
		int pas = 0;
		while(st.hasMoreTokens()) {
			resultArray[pas++] = st.nextToken();
		}
		Map<String,Object> m2 = new HashMap<>();
		m2.put("inputTextArray",resultArray);
		m2.put("quizeText", result);
		m2.put("keyword", keyword);
		m2.put("answer", answer);
		m2.put("count", num);
		return m2;
	}
}