package com.cosmetic.shop.order;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cosmetic.shop.cart.CartService;
import com.cosmetic.shop.cart.CartVO;
import com.cosmetic.shop.common.utils.FileUtils;
import com.cosmetic.shop.member.MemberService;
import com.cosmetic.shop.member.MemberVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequestMapping("/order/*")
@Controller
@RequiredArgsConstructor
public class OrderController {
	
	public final OrderService orderService;
	public final CartService cartService;
	public final MemberService memberService;
	

	// 상품이미지 관련작업기능
	private final FileUtils fileUtils;
		
	@Value("${com.ezen.upload.path}")
	private String uploadPath;
	
	// 1)장바구니에서 주문클릭
	// 2)상품리스트, 상품상세에서 Buy(구매) 버튼클릭
	@GetMapping("/order_info")
	public void order_info(CartVO vo, String type, HttpSession session, Model model) throws Exception{
		
		// 장바구니 추가
		// 로그인 사용자아이디
		String m_id = ((MemberVO)session.getAttribute("login_auth")).getM_id();
		vo.setM_id(m_id);
		
		if(type.equals("buy")) cartService.cart_add(vo);// type=buy일 경우(buy버튼)
		
		// 장바구니(구매정보)
		List<Map<String, Object>> cartDetails = cartService.getCartDetailsByUserId(m_id);
		
		// 날짜폴더의 역슬래쉬 \ 를 / 로 변환작업
		cartDetails.forEach(cartVO -> {
			cartVO.put("pro_up_folder", cartVO.get("pro_up_folder").toString().replace("\\", "/"));
			
		});
		
		String item_name = "";// 초기값
		
		if(cartDetails.size() == 1) {// cartDetails의 개수
			item_name = (String) cartDetails.get(0).get("pro_name");
		}else {
			item_name = (String) cartDetails.get(0).get("pro_name") + " 외" + cartDetails.size();
			
		}
		
		// 주문정보
		model.addAttribute("cartDetails", cartDetails);
		
		// 총주문금액
		model.addAttribute("order_total_price", cartService.getCartTotalPriceByUserId(m_id));
		
		// 로그인한 사용자정보
		MemberVO memberVO = memberService.modify(m_id);
		model.addAttribute("memberVO", memberVO);
		
		// 결제진행 할때 사용하는 용도로 
		model.addAttribute("item_name", item_name);
		model.addAttribute("quantity", cartDetails.size());
		
	}
	
	// 상품목록 이미지출력하기.. 클라이언트에서 보낸 파라미터명 스프링의 컨트롤러에서 받는 파라미터명이 일치해야 한다.
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		return fileUtils.getFile(uploadPath + "\\" + dateFolderName, fileName);
	}
	
	// 계좌이체인 경우만 실행.  카카오페이는 사용안함.
	@PostMapping("/order_save")
	public String order_save(OrderVO vo, HttpSession session, String p_method,
			String account_transfer,String sender, RedirectAttributes rttr) throws Exception {
		
		String m_id = ((MemberVO) session.getAttribute("login_auth")).getM_id();
		vo.setM_id(m_id);
		
		// 계좌이체/국민은행/홍길동
		String p_method_info = p_method + "/" + account_transfer + "/" + sender;
		
		orderService.order_process(vo, m_id, p_method_info);
		
		return "";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
