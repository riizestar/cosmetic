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
import com.cosmetic.shop.common.utils.PageMaker;
import com.cosmetic.shop.common.utils.SearchCriteria;
import com.cosmetic.shop.mail.EmailDTO;
import com.cosmetic.shop.mail.EmailService;
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
	public final EmailService emailService;
	
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
		
		// 결제진행할 때 타임리프 페이지에서사용할 용도
		model.addAttribute("item_name", item_name);// 예> 상품A외2건
		model.addAttribute("quantity", cartDetails.size()); // 주문수량
		
	}
	
	// 상품목록 이미지출력하기.. 클라이언트에서 보낸 파라미터명 스프링의 컨트롤러에서 받는 파라미터명이 일치해야 한다.
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		return fileUtils.getFile(uploadPath + "\\" + dateFolderName, fileName);
	}
	
	// 계좌이체인 경우만 실행.  카카오페이는 사용안함.
	// 주문페이지에서 결제하기 클릭 - 무통장입금 선택
	@PostMapping("/order_save")
	public String order_save(OrderVO vo, HttpSession session, String p_method,
			String account_transfer,String sender, RedirectAttributes rttr) throws Exception {
		
		String m_id = ((MemberVO) session.getAttribute("login_auth")).getM_id();
		vo.setM_id(m_id);
		
		((MemberVO) session.getAttribute("login_auth")).getM_email();
		
		// 계좌이체/국민은행/홍길동
		String p_method_info = p_method + "/" + account_transfer + "/" + sender;
		
		orderService.order_process(vo, m_id, p_method_info);
		
		//log.info("주문번호 : " + vo.getOrd_code());
		
		// 주문내역을 보여주기 위한 주소에서 사용하는 파라미터
		// /order/order_result?ord_code=주문번호&ord_mail=메일주소
		rttr.addAttribute("ord_code", vo.getOrd_code());
		rttr.addAttribute("ord_mail", vo.getOrd_mail());
		
		return "redirect:/order/order_result";
		
	}
	
	// 전역변수선언 : 기억장소를 생성함
	int order_total_price;// 주문총금액 데이터 저장
	
	// 주문결과내역 : 주문결제기능이 끝나고 마지막 화면에 주문번호에 해당한 주문한 내역을 보여줌
	@GetMapping("/order_result")
	public void order_result(Integer ord_code,String ord_mail, HttpSession session, Model model) throws Exception{
		
		String m_email = ((MemberVO)session.getAttribute("login_auth")).getM_email();
		
		// 반드시 0으로 초기화해야함.
		// 안하면 세션이 유지된 상태에서 새로운 구매를 해 총금액이 누적됨
		order_total_price = 0;
		
		// 주문결과내역(주문번호)
		// db에서 읽어온 정보중에 주문금액도 존재하고 있어서, 이걸 이용하여 전체금액을 계산
		List<Map<String, Object>> order_info = orderService.getOrdInfoByOrd_code(ord_code);// 주문결제에
		
		// 날짜폴더의 역슬래시 \를 /로 변환
		// 한 주문이 여러 개의 상품을 포함하고 있을 수 있기 때문에, forEach 각 항목에 대해 개별적으로 작업 
		// order_info : 여러 개의 주문 항목이 포함된 리스트
		// o_Info : order_info리스트의 각 하나의 항목
		// 날짜폴더의 역슬래쉬 \ 를 / 로 변환작업
		order_info.forEach(o_Info -> {
			o_Info.put("pro_up_folder", o_Info.get("pro_up_folder").toString().replace("\\", "/"));
			
			// 총주문금액 저장
			order_total_price += ((int) o_Info.get("dt_amount") * (int) o_Info.get("dt_price"));
		});
		
		// 주문한 내역을 메일로 발송하는 작업
		// EmailDTO클래스를 이용하여 객체생성
		// 이메일 발송을 위한 정보를 준비하는 과정
		EmailDTO dto = new EmailDTO("CosmeticShop","CosmeticShop",m_email,"주문내역","주문내역");
		
		emailService.sendMail("mail/orderConfirmation", dto, order_info, order_total_price);// 실제 이메일을 발송하는 과정
		
		//log.info("총주문금액: " + order_total_price);
		//log.info("이메일 :" + ord_mail);
		
		// 타임리프에서 주문내역과 주금 총금액을 표시한다.
		model.addAttribute("order_info", order_info);
		model.addAttribute("order_total_price", order_total_price);
	}
	
	// order_list : 주문목록  review_manage: 주문목록중 배송완료 대상
	@GetMapping(value = {"/order_list"})
	public void order_list(SearchCriteria cri, HttpSession session, Model model) throws Exception {
		
		String m_id = ((MemberVO) session.getAttribute("login_auth")).getM_id();
		
		cri.setPerPageNum(5);
		
		
		// cri는 목록에 페이지마다 보여주는 출력개수정보, 페이지번호 클릭할 때 선택한 페이지변수
		List<Map<String, Object>> order_list = orderService.getOrderInfoByUser_id(m_id, cri); // 사용
		
		// 날짜폴더의 역슬래쉬 \ 를 / 로 변환작업
		order_list.forEach(o_Info -> {
			o_Info.put("pro_up_folder", o_Info.get("pro_up_folder").toString().replace("\\", "/"));			
		});
		
		model.addAttribute("order_list", order_list);
		
		
		// 1	2	3	4	5
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri); // 사용
		pageMaker.setTotalCount(orderService.getOrderCountByUser_id(m_id));  // 13
		
		model.addAttribute("pageMaker", pageMaker);
		
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
