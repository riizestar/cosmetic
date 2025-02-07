package com.cosmetic.shop.review;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cosmetic.shop.member.MemberVO;
import com.cosmetic.shop.product.ProductService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController 
@RequiredArgsConstructor
@RequestMapping("/review/*") // /review로 시작하는 모든주소는 ReviewController 클래스가 처리한다.
public class ReviewController {
	
	private final ReviewService reviewService;
	private final ProductService productService;
	
	// Create(등록)
	// @RequestBody ReviewVO vo : 클라이언트에서 전송되어 온 JSON문자열 데이타를 ReviewVO클래스의 필드로 매핑(변환)하는 작업.
	// consumes = "application/json" : 클라이언트에서 전송하는 데이터는 JSON 형식
	// produces = {MediaType.TEXT_PLAIN_VALUE} : 메서드가 반환하는 응답은 plain text 형식
	@PostMapping(value = "/review_save",consumes = "application/json",produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> review_save(@RequestBody ReviewVO vo, HttpSession session) throws Exception {
		
		String m_id = ((MemberVO)session.getAttribute("login_auth")).getM_id();
		vo.setM_id(m_id);
		
		log.info("상품후기: " + vo);
		
		ResponseEntity<String> entity = null;
		
		reviewService.review_save(vo);
		
		return entity;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
