package com.morelang.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "vocaquiz_logs")
public class VocaQuizLog {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long logId;
	
	@ManyToOne(targetEntity=Member.class, fetch=FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	@Column
	private int answerCnt;
	
	@Column
	private int wrongCnt;
	
	@Column
	private int allCnt;
	
	@Column
	private double accRate;
	
	@Column
	private String country;
	
	@Column
	@UpdateTimestamp
	private Date quizTime;
	
}
