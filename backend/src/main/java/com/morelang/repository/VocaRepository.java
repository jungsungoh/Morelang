package com.morelang.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.morelang.dto.Voca;
import com.morelang.dto.VocaSub;

public interface VocaRepository extends JpaRepository<Voca, Long>{
	public Optional<Voca> findByMember_idAndVocaId(Integer userid, Long vocaid);
	public List<VocaSub> findByMember_id(Integer userid);
	public Page<VocaSub> findByMember_id(Integer userid, Pageable pageable);
	public Page<VocaSub> findByMember_idAndCountryIn(Integer userid,String[] Country, Pageable pageable);
	public List<VocaSub> findByMember_idAndIsLearnAndCountry(Integer userid,Boolean isLearn,String country);
	public List<VocaSub> findByMember_idAndIsLearn(Integer userid,Boolean isLearn);
	public List<Voca> findByMember_idAndUsedAndCountry(Integer userid, boolean used, String country);
	public List<Voca> findByMember_idAndUsed(Integer userid, boolean used);
	public List<Voca> findByCountryAndUsed(String country, boolean used);
	public List<Voca> findByUsed(boolean used);
	public List<VocaSub> findByMember_idAndCountry(Integer userid, String Country);
	@Query(value = "select distinct * from vocas  where each_mean not like CONCAT('%',:mean,'%') and used = false", nativeQuery = true)
	public List<Voca> distinctMean(String mean);
	
	@Query(value = "SELECT DISTINCT country FROM vocas where member_id=:userid",nativeQuery = true)
	public List<String> findDistinctCountry(Integer userid);
	
	@Transactional
	@Modifying
	@Query(value = "update vocas v set v.used = false where member_id=:userid and v.used = true",nativeQuery = true)
	public int updateStatus(Integer userid);
}
