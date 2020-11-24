
package com.morelang.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Video {

	private String id;
	private String title;
	private String description;
	private String publishedAt;
	private String channelId;
	private String channelTitle;
	private String defaultLanguage;
	private String defaultAudioLanguage;
	private long duration;

}
