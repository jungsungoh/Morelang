package com.morelang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morelang.dto.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	public Optional<Member> findByAccessToken(String accessToken);
	public Optional<Member> findByUserid(String userid);
	public Member findByName(String name);
}
