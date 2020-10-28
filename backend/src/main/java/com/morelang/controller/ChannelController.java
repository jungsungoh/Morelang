package com.morelang.controller;

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

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class ChannelController {
	@Autowired
	ChannelService channelService;

	@GetMapping("/channel")
	public ResponseEntity<Channel> channel(@RequestParam("id") String id) {

		Channel channel = null;
		try {
		} catch (Exception e) {
			e.printStackTrace();
			Webhook.send(this.getClass().toString(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(channel, HttpStatus.OK);
	}

	@GetMapping("/playlist")
	public ResponseEntity<Playlist> playlist(@RequestParam("id") String id,
			@RequestParam(value = "token", required = false) String token) {

		Playlist playlist = null;
		try {
		} catch (Exception e) {
			e.printStackTrace();
			Webhook.send(this.getClass().toString(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(playlist, HttpStatus.OK);
	}

}
