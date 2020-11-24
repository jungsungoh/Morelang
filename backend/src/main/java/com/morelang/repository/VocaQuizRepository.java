package com.morelang.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.morelang.dto.VocaQuizLog;
import com.morelang.dto.VocaQuizLogSub;

public interface VocaQuizRepository extends JpaRepository<VocaQuizLog, Long>{
	List<VocaQuizLogSub> findByMember_idAndCountry(Integer userid,String country,Sort sort);
	
	@Query(value = "SELECT DISTINCT country FROM vocaquiz_logs where member_id=:userid",nativeQuery = true)
	public List<String> findDistinctCountry(Integer userid);
}
