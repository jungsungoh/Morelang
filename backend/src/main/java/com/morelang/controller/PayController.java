package com.morelang.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morelang.dto.PageRequest;
import com.morelang.dto.PayApprove;
import com.morelang.dto.PayReady;
import com.morelang.dto.pointCharge;
import com.morelang.service.PayService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
public class PayController {
	@Autowired
	PayService payService;

	@GetMapping("/user/pay/ready")
	public ResponseEntity<PayReady> ready(@RequestParam("item_name") String item_name,
			@RequestParam("total_amount") String total_amount, @RequestParam(value = "id", required = false) String id, HttpServletRequest request) {

		PayReady payReady = null;
		try {
			payReady = payService.ready(item_name, total_amount, id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(payReady, HttpStatus.OK);
	}

	@GetMapping("/user/pay/approve")
	public ResponseEntity<PayApprove> approve(HttpServletResponse response,@RequestParam("tid") String tid,
			@RequestParam("pg_token") String pg_token, @RequestParam("total_amount") String total_amount,
			HttpServletRequest request) {
		System.out.println(response.getHeaderNames());
		String accessToken = response.getHeader("accessToken");
		PayApprove payApprove = null;
		try {
			payApprove = payService.approve(accessToken,tid, pg_token, total_amount);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(payApprove, HttpStatus.OK);
	}
	
	@GetMapping("/user/pay/my-point")
	@ApiOperation(value = "[포인트 조회] 내가 가지고 있는 포인트의 양을 조회함")
	public ResponseEntity<Integer> MyPoint(HttpServletResponse response){
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<Integer>(payService.MyPoint(accessToken),HttpStatus.OK);
		
	}
	
	@GetMapping("/user/pay/my-pointlogs")
	@ApiOperation(value = "[포인트 내역 조회] 포인트 충전, 사용등의 내역을 조회합니다.")
	public ResponseEntity<Page<pointCharge>> MyPointLogs(HttpServletResponse response, PageRequest pageable){
		String accessToken = response.getHeader("accessToken");
		return new ResponseEntity<Page<pointCharge>>(payService.PointLogs(accessToken, pageable.charge()),HttpStatus.OK);
	}
}
