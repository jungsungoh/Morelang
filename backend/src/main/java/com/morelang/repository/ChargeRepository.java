package com.morelang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.morelang.dto.pointCharge;

public interface ChargeRepository extends JpaRepository<pointCharge, Long>{
	public Page<pointCharge> findByMember_id(Integer userid,Pageable pageable); 
}
