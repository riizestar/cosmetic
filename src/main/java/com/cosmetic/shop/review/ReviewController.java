package com.cosmetic.shop.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cosmetic.shop.common.utils.SearchCriteria;
import com.cosmetic.shop.member.MemberVO;
import com.cosmetic.shop.product.ProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//리뷰 : 상품상세페이지에서 사용목적. REST API 개발형태로 작업
@Slf4j
@RestController 
@RequiredArgsConstructor
@RequestMapping("/review/*") // /review로 시작하는 모든주소는 ReviewController 클래스가 처리한다.
public class ReviewController {
	
	private final ReviewService reviewService;
	private final ProductService productService;
	
	// 자바스크립트로 작업하기 이전에 테스트를 postman 툴로 확인해본다.
	// 1)상품후기목록- List<ReviewVO)  2)페이징 데이타 작업 PageMaker 2가지를 하나의 Map으로 관리
	@GetMapping("rev_list/{pro_num}/{page}")
	public ResponseEntity<Map<String, Object>> rev_list(@PathVariable("pro_num") Integer pro_num,
												@PathVariable("page") int page) throws Exception{
		log.info("상품코드: " + pro_num);
		log.info("페이지: " + page);
		
		ResponseEntity<Map<String, Object>> entity = null;
		
		// 데이타가 타입이 다른 경우 Map컬렉션에 저장하여 하나로 관리
		Map<String, Object> map = new HashMap<>();
		
		// 1)상품후기 및 답변목록
		SearchCriteria cri = new SearchCriteria();
		cri.setPerPageNum(10);
		cri.setPage(page);
		
		List<ReviewVO> rev_list = reviewService.rev_list(pro_num, cri);
				
		// key가 자바스크립트의 ajax 변수에서 참조한다.
		map.put("rev_list", rev_list);
		
		entity = new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		
		return entity;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
		
		// 상품후기등록
		reviewService.review_save(vo);
		
		// 상품후기 카운트 읽어오는 작업.
		int review_count = productService.review_count_pro_info(vo.getPro_num());
		
		entity = new ResponseEntity<String>(String.valueOf(review_count),HttpStatus.OK);
		
		return entity;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
