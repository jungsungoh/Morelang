package com.morelang.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morelang.dto.HistoryVideo;
import com.morelang.dto.PageRequest;
import com.morelang.dto.recommendChannel;
import com.morelang.service.HistoryService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class HistoryController {
	
	@Autowired
	HistoryService historyService;
	
	@PostMapping("/user/watch-video")
	@ApiOperation(value="[동영상 조회] 사용자가 동영상을 눌렀을 때, 100 포인트를 결제하는 행위 입니다. 이미 본건 결제 안함 [return] 성공하면 success, 사용자 인증에 실패하면 fail, 포인트가 없으면 잔액부족")
	public ResponseEntity<String> watchVideo(HttpServletResponse response, @RequestBody HistoryVideo video){
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<String>(historyService.watchVideo(accessToken, video),HttpStatus.OK);
	}
	
	@GetMapping("/user/myvideo")
	@ApiOperation(value="[결제한 동영상- 페이징 처리] 내가 결제한 동영상을 최신 순(DESC)/안본지 오래된(ASC) 순으로 가져옵니다.")
	public ResponseEntity<List<HistoryVideo>> myVideo(HttpServletResponse response, PageRequest pageable){
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<List<HistoryVideo>>(historyService.myVideoList(accessToken, pageable.videoTime()),HttpStatus.OK);
	}
	@GetMapping("/user/myvideosize")
	@ApiOperation(value="[결제한 동영상 크기] 내가 결제한 동영상 size를 가져옵니다.")
	public ResponseEntity<Integer> myVideo(HttpServletResponse response){
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<Integer>(historyService.myVideoListSize(accessToken),HttpStatus.OK);
	}
	@PostMapping("/user/iswatched")
	@ApiOperation(value="[결제한 동영상- 페이징 처리] 내가 본 동영상인지 아닌지 true/false return, 조회수 증가 여기서 처리했음")
	public ResponseEntity<Boolean> iswatched(HttpServletResponse response, @RequestBody HistoryVideo video){
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<Boolean>(historyService.is_view(accessToken, video),HttpStatus.OK);
	}
	@GetMapping("/newuser/recommend-list")
	@ApiOperation(value="[국가별 추천채널] 국가 코드를 주면 해당 국가의 추천채널들을 random 하게 return")
	public ResponseEntity<List<recommendChannel>> recommend(@RequestParam(required=false) String country){
		return new ResponseEntity<List<recommendChannel>>(historyService.recommendList(country),HttpStatus.OK);
	}
	@GetMapping("/newuser/highcount")
	@ApiOperation(value="[조회수 높은 동영상 찾기] defaultlang 값에 따라 분류 가능")
	public ResponseEntity<Page<HistoryVideo>> highcount(@RequestParam(required=false) String defaultlang, PageRequest pageable){
		return new ResponseEntity<Page<HistoryVideo>>(historyService.highCountVideo(defaultlang, pageable.channelCount()),HttpStatus.OK);
	}
}
