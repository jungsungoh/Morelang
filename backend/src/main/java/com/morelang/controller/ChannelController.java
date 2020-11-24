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
import com.morelang.dto.Channel;
import com.morelang.dto.Playlist;
import com.morelang.service.ChannelService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class ChannelController {
	@Autowired
	ChannelService channelService;

	@GetMapping("/newuser/channel")
	@ApiOperation(value = "예)UCkuA_gDjISfGgbdp02BUwyQ")
	public ResponseEntity<Channel> channel(@RequestParam("id") String id, HttpServletRequest request) {

		Channel channel = null;
		try {
			channel = channelService.getInfo(id);
		} catch (Exception e) {
			e.printStackTrace();
			Webhook.send(request, this.getClass().toString(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(channel, HttpStatus.OK);
	}

	@GetMapping("/newuser/playlist")
	public ResponseEntity<Playlist> playlist(@RequestParam("id") String id,
			@RequestParam(value = "token", required = false) String token, HttpServletRequest request) {

		Playlist playlist = null;
		try {
			playlist = channelService.getItems(id, token);
		} catch (Exception e) {
			e.printStackTrace();
			Webhook.send(request, this.getClass().toString(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(playlist, HttpStatus.OK);
	}

}
