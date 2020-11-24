package com.morelang.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morelang.service.TranslateService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class TranslateController {
	@Autowired
	TranslateService translateService;

	@GetMapping("/newuser/translate")
	@ApiOperation(value = "한국어kr / 영어en / 일본어jp / 중국어cn / 베트남어vi / 인도네시아어id / 아랍어ar / 뱅갈어bn / 독일어de / 스페인어es / 프랑스어fr / 힌디어hi / 이탈리아어it / 말레이시아어ms / 네덜란드어nl / 포르투갈어pt / 러시아어ru / 태국어th / 터키어tr")
	public ResponseEntity<String> translate(@RequestParam("src_lang") String src_lang,
			@RequestParam("target_lang") String target_lang, @RequestParam("query") String query,
			HttpServletRequest request) {

		String s = null;
		try {
			s = translateService.translate(src_lang, target_lang, query);
		} catch (Exception e) {
			e.printStackTrace();
//			Webhook.send(request, this.getClass().toString(), e);
			return new ResponseEntity<>(null, HttpStatus.OK);
		}

		return new ResponseEntity<>(s, HttpStatus.OK);
	}

}
