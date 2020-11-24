package com.morelang.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "scraps")
public class Scrap {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int scrapId;
	
	@ManyToOne(targetEntity=Member.class, fetch=FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	@Column
	private String sentence;
	
	@Column
	private String memo;
	
	@Column
	private String country;
	
	private String videourl;

	public Scrap(String sentence, String memo, String country, String videourl) {
		super();
		this.sentence = sentence;
		this.memo = memo;
		this.country = country;
		this.videourl = videourl;
	}
	
}
