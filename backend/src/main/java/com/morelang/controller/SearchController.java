package com.morelang.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morelang.config.Webhook;
import com.morelang.dto.Search;
import com.morelang.service.SearchService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class SearchController {
	@Autowired
	SearchService searchService;

	@GetMapping("/newuser/search")
	@ApiOperation(value = "start: 시작할 index(0/10/20/...10단위)")
	public ResponseEntity<List<Search>> search(@RequestParam("q") String q, @RequestParam("start") String start,
			HttpServletRequest request) {

		List<Search> result = null;
		try {
			result = searchService.search(q, start);
		} catch (Exception e) {
			e.printStackTrace();
			Webhook.send(request, this.getClass().toString(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
