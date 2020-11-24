package com.morelang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morelang.dto.Learn;

public interface LearnRepository extends JpaRepository<Learn, Long>{
	
}
