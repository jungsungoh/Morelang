package com.morelang.service;

import java.util.List;

import com.morelang.dto.Search;

public interface SearchService {

	public List<Search> search(String q, String start) throws Exception;
}
