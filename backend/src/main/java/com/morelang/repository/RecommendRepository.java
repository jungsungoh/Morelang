package com.morelang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morelang.dto.recommendChannel;

public interface RecommendRepository extends JpaRepository<recommendChannel, Long>{
	List<recommendChannel> findByCountry(String country);
}
