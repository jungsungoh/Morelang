package com.morelang.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.morelang.dto.PayApprove;
import com.morelang.dto.PayReady;
import com.morelang.dto.pointCharge;

public interface PayService {
	public PayReady ready(String item_name, String total_amount, String id) throws Exception;
	public Integer MyPoint(String accessToken);
	public Page<pointCharge> PointLogs(String accessToken, Pageable pageable);
	public PayApprove approve(String aceessToken,String tid, String pg_token, String total_amount) throws Exception;
}
