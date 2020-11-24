package com.morelang.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morelang.service.LearnService;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class LearnController {
	@Autowired
	LearnService learnService;

	@PostMapping("/newuser/puzzle")
	public ResponseEntity<?> WorkPuzzle(@RequestParam("inputText") String input) {
		Map<String, Object> map = null;
		try {
			map = learnService.WordPuzzle(input);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	@PostMapping("/newuser/puzzletest")
	public ResponseEntity<?> Puzzletest(@RequestParam("inputText") String input) throws IOException {
		return new ResponseEntity<Map<String, Object>>(learnService.puzzeltest(input), HttpStatus.OK);
	}
}
