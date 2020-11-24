package com.morelang.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morelang.config.Webhook;
import com.morelang.dto.Video;
import com.morelang.service.VideoService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class VideoController {
	@Autowired
	VideoService videoService;

	@GetMapping("/newuser/video")
	@ApiOperation(value = "예)lutr9KxY-8s")
	public ResponseEntity<Video> video(@RequestParam("id") String id, HttpServletRequest request) {

		Video video = null;
		try {
			video = videoService.getInfo(id);
		} catch (Exception e) {
			e.printStackTrace();
			Webhook.send(request, this.getClass().toString(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(video, HttpStatus.OK);
	}

}
