package com.cosmetic.shop.kakaopay;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cosmetic.shop.member.MemberVO;
import com.cosmetic.shop.order.OrderService;
import com.cosmetic.shop.order.OrderVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kakao/*")
@Slf4j
public class KakaopayController {
	
	private final KakaopayService kakaopayService;
	private final OrderService orderService;
	
	// 전역변수. 1차준비요청, 2차결제승인요청
	private String m_id;
	private OrderVO order_info;
	private int order_total_price;
	
	@PostMapping("kakaoPay") // 카카오페이 선택후 결제하기 버튼클릭하면 ajax방식으로 요청. 
	public ResponseEntity<ReadyResponse> kakaopay(OrderVO vo, String item_name, int quantity,HttpSession session) {// order_info에서 item_name, quantity
	
		// 1) 로그인한 사용자 정보 가져오기
		m_id = ((MemberVO) session.getAttribute("login_auth")).getM_id();
		vo.setM_id(m_id);
		
		log.info("주문정보: " + vo);
		
		this.order_info = vo;
		this.order_total_price = vo.getOrd_price();// 할인 해서 결제금액과 주문금액이 다를 수 있는데???
		
		// log.info("카카오페이 정보: " + kakaopayService.getKakaoPayProperties().toString());
		
		ResponseEntity<ReadyResponse> entity = null;
		
		// 2. 가맹점 주문번호 생성
		// 주문테이블의 주문번호를 사용하려했으나, 결제시 이슈가 없는 관계로
		// 가맹점 관리에서 구분해야 하므로, 회원아이디 및 주문날자 정보 조합사용.
		String partner_order_id = "cosmeticshop[" + m_id + "] - " + new Date().toString();
		// "cosmeticshop[user123] - Sun Jan 27 15:30:00 GMT 2025"
		
		// 3. 카카오페이 결제 준비 요청
		ReadyResponse readyResponse = kakaopayService.ready(partner_order_id, m_id, item_name, quantity, order_total_price, 0);
		
		log.info("결제준비요청 응답결과" + readyResponse.toString());
		
		// 4. 응답을 ResponseEntity로 감싸서 반환.   readyResponse 객체의 정보가 ajax호출한 쪽으로 넘어간다.
		entity = new ResponseEntity<ReadyResponse>(readyResponse, HttpStatus.OK);
		
		return entity;// jquery 결제하기 이벤트로 제어가 넘어간다.
	}
	
	// (카카오페이 API서버가 호출함)
	// 카카오페이 개발자 애플리케이션 플랫폼에 설정
	// 결제준비요청이 성공되면, QR코드 페이지에서 스캔작업 진행 후 pg_token 값이 전달되고 호출됨
	@GetMapping("/approval")
	public String approval(String pg_token, RedirectAttributes rttr) {
		
		log.info("pg_token: " + pg_token);
		
		// 결제승인요청
		String response = kakaopayService.approve(pg_token);
		
		// 결제승인요청의 성골 응답파라미터로 aid를 확인
		if(response.contains("aid")) {
			//OrderService 파일에서 주문관련작업
			orderService.order_process(this.order_info, m_id, "카카오페이");
		}
		
		rttr.addAttribute("ord_code", order_info.getOrd_code());
		// /order/order_result?ord_code=주문번호
		return "redirect:/order/order_result";
	}
	
	// 결제가 취소
	@GetMapping("/cancle")
	public String cancle() {
		
		return "/order/order_cancel";
	}
	
	// 결제가 실패
	@GetMapping("/fail")
	public String fail() {
		
		return "/order/order_fail";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
