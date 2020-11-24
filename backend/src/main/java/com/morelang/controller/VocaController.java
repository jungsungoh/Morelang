package com.morelang.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import com.morelang.dto.Voca;
import com.morelang.dto.VocaSub;
import com.morelang.service.VocaService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class VocaController {
	@Autowired
	VocaService vocaService;
	
	@PostMapping("/user/regist-voca")
	@ApiOperation(value = "[단어등록]단어장에 단어 등록하기")
	public ResponseEntity<?> registVoca(HttpServletResponse response, @RequestBody Voca voca) {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<String>(vocaService.registVoca(accessToken, voca),HttpStatus.OK);
	}
	@GetMapping("/user/myvoca")
	@ApiOperation(value = "[내 단어장 보기- 페이징] 내 단어장 확인하기")
	public ResponseEntity<?> MyVoca(HttpServletResponse response,@RequestParam(required=false) String[] country, PageRequest pageable) {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<Page<VocaSub>>(vocaService.MyVoca(accessToken, pageable.vocaof(), country),HttpStatus.OK);
	}
	@PutMapping("/user/update-voca")
	@ApiOperation(value = "[단어 갱신] 해당 단어 업데이트하기")
	public ResponseEntity<?> updateVoca(HttpServletResponse response, @RequestBody Voca voca) {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<String>(vocaService.updateVoca(accessToken, voca),HttpStatus.OK);
	}
	@DeleteMapping("/user/delete-voca")
	@ApiOperation(value = "[단어장에서 단어 제거] 단어장에서 해당 단어 제거")
	public ResponseEntity<?> deleteVoca(HttpServletResponse response, @RequestParam("vocaId") Long vocaId) {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<String>(vocaService.DeleteVoca(accessToken, vocaId),HttpStatus.OK);
	}
	@PutMapping("/user/islearn")
	@ApiOperation(value = "[단어 학습 완료 on/off] 해당 단어 학습했는지 안했는지 on/off하기")
	public ResponseEntity<?> isLearn(HttpServletResponse response, @RequestParam("vocaId") Long vocaId) {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<String>(vocaService.makeLearn(accessToken, vocaId),HttpStatus.OK);
	}
	@PutMapping("/user/change-mean")
	@ApiOperation(value = "[단어 의미 바꾸기] 해당 단어에 대한 의미를 변경합니다.")
	public ResponseEntity<?> changeMean(HttpServletResponse response,@RequestParam("vocaid") Integer vocaId,@RequestParam("mean") String mean) throws IOException {
		String accessToken = response.getHeader("accessToken");	
		return new ResponseEntity<String>(vocaService.changeMean(accessToken, vocaId, mean),HttpStatus.OK);
	}
	
	@GetMapping("/user/myvoca-country")
	@ApiOperation(value = "[내 단어장에 저장된 국가들] 내 단어장에 저장된 국가 보기")
	public ResponseEntity<?> vocaCountry(HttpServletResponse response)  {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<List<String>>(vocaService.myVocaCountry(accessToken),HttpStatus.OK);
	}
	
	@GetMapping("/user/myvoca-quize")
	@ApiOperation(value = "[내 단어장에 저장된 국가들을 활용한 퀴즈] 내 단어장에 저장된 국가를 활용해 퀴즈내기")
	public ResponseEntity<?> vocaQuize(HttpServletResponse response, @RequestParam(required=false) String country, @RequestParam("index") Integer index) throws IOException {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<Map<String,Object>>(vocaService.vocaQuize(accessToken, country, 0),HttpStatus.OK);
	}
	
	@GetMapping("/user/init-quiz")
	@ApiOperation(value = "[퀴즈 시작 전] 퀴즈 시작 전 초기화")
	public ResponseEntity<?> initQuiz(HttpServletResponse response) {
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<String>(vocaService.initQuiz(accessToken),HttpStatus.OK);
	}
	
	@PostMapping("/user/end-quiz")
	@ApiOperation(value = "[퀴즈 종료] 퀴즈 종료 하고 기록")
	public ResponseEntity<?> endQuiz(HttpServletResponse response, @RequestParam(required=false) String country,@RequestParam("answer_cnt") Integer answer_cnt , @RequestParam("all_cnt") Integer all_cnt){
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<String>(vocaService.QuizResult(accessToken,country, answer_cnt, all_cnt),HttpStatus.OK);
	}
	@GetMapping("/user/quiz-log")
	@ApiOperation(value = "[퀴즈 그래프용] 대상자에 대한 퀴즈 기록 정보들 국가별로 줌")
	public ResponseEntity<?> QuizLog(HttpServletResponse response){
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<Map<String,Object>>(vocaService.QuizLogData(accessToken),HttpStatus.OK);
	}
}
