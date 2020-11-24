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

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "learn")
public class Learn {
	@Id
	@Column(name="subid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int subid;
	@Column
	String lemma;
	@Column
	String tag;
}
