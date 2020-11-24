
package com.morelang.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Playlist {

	private String token;
	private List<PlaylistItem> items;

}
