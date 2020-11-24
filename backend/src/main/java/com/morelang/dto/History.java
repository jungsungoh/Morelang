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
@Table(name = "historys")
public class History {
	@Id
	@Column(name = "history_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int historyId;
	
	@ManyToOne(targetEntity=Member.class, fetch=FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	@ManyToOne(targetEntity=HistoryVideo.class, fetch=FetchType.EAGER)
	@JoinColumn(name="video_id")
	private HistoryVideo video;
                                                                                                                                                                                                                                                                                                                                                           
	@Column
	@UpdateTimestamp
	private Date viewDay;
	
	public History(int historyId, Member member, HistoryVideo video, Date viewDay) {
		super();
		this.historyId = historyId;
		this.member = member;
		this.video = video;
		this.viewDay = viewDay;
	}
}
