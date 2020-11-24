package com.morelang.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "videos")
public class HistoryVideo {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int vid;
	
	@Column
	private String title;
	
	@Column
	private String thumbnail;
	
	@Column
	private String youtubeVideoid;
	
	@Column
	private String defaultLanguage;
	
	@Column
	private int count;	
	
}
