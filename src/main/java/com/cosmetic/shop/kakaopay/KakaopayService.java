package com.cosmetic.shop.kakaopay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
		
		// 2)Request param. 준비요청을 위한 파라미터 데이터 작업
		ReadyRequest readyRequest = ReadyRequest.builder()
				
				.cid(cid)
				.partner_order_id(partner_order_id)
				.partner_user_id(partner_user_id)
				.item_name(item_name)
				.quantity(quantity)
				.total_amount(total_amount)
				.tax_free_amount(tax_free_amount)
				.approval_url(item_name)
				.cancel_url(item_name)
				.fail_url(item_name)
				
				.build();
		
		//data request. 결제준비요청에 보낼 헤더와 파라미터를 가지고 있는 객체
		HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);
		
		// 카카오페이 API서버에 결제준비요청. 핵심코드 **********************************  중요
		// RestTemplate을 사용하여 postForEntity() 메서드로 카카오페이 API에 POST 요청을 보내고, 응답 받는다(response)
		ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(//postForEntity()는 ResponseEntity 객체를 반환
				readUrl, entityMap, ReadyResponse.class);//ReadyResponse.class: 응답을 받을 클래스 타입(ReadyResponse)
		
		// 응답데이터. 준비요청에 의하여 카카오페이 API로 부터 넘어온 데이타를 받기위한 작업 
		ReadyResponse readyResponse = response.getBody();
		// getBody()는 ResponseEntity 객체에서 응답 본문을 추출하는 메서드. 
		// 서버에서 반환된 데이터(JSON 응답)를 Java 객체로 변환
		
		log.info("1차 준비요청응답정보" + readyResponse);
		
		// 1차 준비요청에 의해 받은 응답정보
		// 전역변수에 응답받은 tid값을 저장하여, 2차결제승인요청에서 사용한다.
		this.tid = readyResponse.getTid();
		this.partner_order_id = partner_order_id;
		this.partner_user_id = partner_user_id;
		
		return readyResponse;
	}
	
	public HttpHeaders getHttpHeaders() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "SECRET_KEY " + secretKey);
		//            헤더이름,카카오페이 API에 필요한 인증을 위해 사용, 외부에서 주입받은 실제 비밀키
		// 이 값을 합쳐서 Authorization 헤더에 설정
		
		headers.set("Content-Type", "application/json;charset=utf-8");
		// JSON 형식이며, UTF-8 문자 인코딩
		return headers;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
