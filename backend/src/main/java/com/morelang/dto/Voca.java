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
@Table(name = "vocas")
public class Voca {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vocaId;
	
	@ManyToOne(targetEntity=Member.class, fetch=FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	@Column
	private String eachVoca;
	
	@Column
	private String eachMean;
	
	@Column
	private String country;
	
	@Column
	private boolean isLearn;

	@Column
	private boolean used;
	public Voca(String eachVoca, String eachMean, String country, boolean isLearn) {
		super();
		this.eachVoca = eachVoca;
		this.eachMean = eachMean;
		this.country = country;
		this.isLearn = isLearn;
	}
	
	
}
