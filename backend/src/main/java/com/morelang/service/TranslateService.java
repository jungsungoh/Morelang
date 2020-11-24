package com.morelang.service;

public interface TranslateService {
	public String translate(String src_lang, String target_lang, String query) throws Exception;
}
