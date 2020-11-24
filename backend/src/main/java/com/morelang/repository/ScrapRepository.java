package com.morelang.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.morelang.dto.Scrap;
import com.morelang.dto.ScrapSub;

public interface ScrapRepository extends JpaRepository<Scrap, Long>{
	public Page<ScrapSub> findByMember_id(Integer userid,Pageable pageable);
	public Optional<Scrap> findByMember_idAndScrapId(Integer userid, Integer scrapid);
}
