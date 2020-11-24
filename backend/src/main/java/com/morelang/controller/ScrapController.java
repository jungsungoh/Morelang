package com.morelang.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morelang.dto.PageRequest;
import com.morelang.dto.Scrap;
import com.morelang.dto.ScrapSub;
import com.morelang.service.ScrapService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class ScrapController {
	@Autowired
	ScrapService scrapService;
	
	@PostMapping("/user/do-scrap")
	@ApiOperation(value = "[스크랩하기] 해당 문장을 scrap합니다.")
	public ResponseEntity<?> doScrap(HttpServletResponse response, @RequestBody Scrap scrap) {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<String>(scrapService.DoScrap(accessToken, scrap),HttpStatus.OK);
	}
	@GetMapping("/user/myscrap")
	@ApiOperation(value = "[내 스크랩 문장 - 페이징] 내가 스크랩한 내용들 확인하기.")
	public ResponseEntity<?> MyScrap(HttpServletResponse response,PageRequest pageable) {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<Page<ScrapSub>>(scrapService.MyScrap(accessToken,pageable.of()),HttpStatus.OK);
	}
	@PutMapping("/user/update-scrap")
	@ApiOperation(value = "[스크랩 문장 업데이트] 스크랩 내용을 업데이트합니다.")
	public ResponseEntity<?> updateScrap(HttpServletResponse response, @RequestBody Scrap scrap) {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<String>(scrapService.updateScrap(accessToken, scrap),HttpStatus.OK);
	}
	@DeleteMapping("/user/delete-scrap")
	@ApiOperation(value = "[스크랩 한 내용 제거] 스크랩내용 삭제하기.")
	public ResponseEntity<?> doScrap(HttpServletResponse response, @RequestParam("scrapId") Integer scrapId) {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<String>(scrapService.DeleteScrap(accessToken, scrapId),HttpStatus.OK);
	}
}
