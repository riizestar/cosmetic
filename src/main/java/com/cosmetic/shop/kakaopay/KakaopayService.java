package com.cosmetic.shop.kakaopay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KakaopayService {
	
	@Value("${readUrl}") 
	private String readUrl;
	
	@Value("${approveUrl}")
	private String approveUrl;//
	
	@Value("${secretKey}")
	private String secretKey;
	
	@Value("${cid}")
	private String cid;//
	
	
	@Value("${approval}") // 결제승인요청시 성공
	private String approval;
	
	@Value("${cancel}")
	private String cancel;// 결제승인요청시 취소
	
	@Value("${fail}")
	private String fail;// 결제승인요청시 실패
	
	private String tid;
	
	private String partner_order_id;//
	
	private String partner_user_id;//
	
	// 1차요청(결제준비요청-ready)
	public ReadyResponse ready(String partner_order_id, String partner_user_id, String item_name, 
			Integer quantity, Integer total_amount, Integer tax_free_amount) {
		
		// 1)Request header
		// 카카오페이 API와의 통신에 필수적인 Authorization 헤더와 Content-Type 헤더를 설정
		HttpHeaders headers = getHttpHeaders();
		
		return ;
	}
	
	public HttpHeaders getHttpHeaders() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", Authorization);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
