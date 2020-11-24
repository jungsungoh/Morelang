package com.morelang.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "members")
public class Member {

	@Id
	@Column
	private int id;
	
	@Column
	private String userid;
	
	@Column
	private String password;
	
	@Column
	private String name;

	@Column
	private String profileImg;

	@Column
	private String providerName;

	@Column
	private String email;

	@Column
	private String accessToken;
	
	@Column
	private int point;
	
	@Column
	private String role;
	
	@Column
	private String refreshToken;
	public Member(String userid, String name, String profileImg, String providerName, String email,
			String accessToken,String refreshToken, int point) {
		super();
		this.userid = userid;
		this.name = name;
		this.profileImg = profileImg;
		this.providerName = providerName;
		this.email = email;
		this.accessToken = accessToken;
		this.point = point;
		this.refreshToken = refreshToken;
	}
}
