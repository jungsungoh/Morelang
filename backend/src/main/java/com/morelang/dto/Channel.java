
package com.morelang.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Channel {

	private String id;
	private String title;
	private String description;
	private String publishedAt;
	private String imgUrl;
	private String channelUrl;
	private String defaultLanguage;
	private String country;
	private String uploads;

}
