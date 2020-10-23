package com.morelang.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morelang.dto.Search;
import com.morelang.service.SearchService;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class SearchController {
	@Autowired
	SearchService searchService;

	@GetMapping("/search")
	public ResponseEntity<List<Search>> search(@RequestParam("q") String q, @RequestParam("start") String start) {
		List<Search> result = null;
		try {
			result = searchService.search(q, start);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
