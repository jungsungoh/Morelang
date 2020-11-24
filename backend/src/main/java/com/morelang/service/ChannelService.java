package com.morelang.service;

import com.morelang.dto.Channel;
import com.morelang.dto.Playlist;

public interface ChannelService {
	public Channel getInfo(String id) throws Exception;

	public Playlist getItems(String id, String token) throws Exception;
}
