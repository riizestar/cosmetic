package com.cosmetic.shop.cart;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cosmetic.shop.common.utils.FileUtils;
import com.cosmetic.shop.member.MemberVO;

import groovyjarjarantlr4.v4.parse.ANTLRParser.exceptionHandler_return;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequestMapping("/cart/*")
@RequiredArgsConstructor
@Controller
public class CartController {

	private final CartService cartService;
	
	// 상품이미지 관련작업기능
	private final FileUtils fileUtils;
	
	@Value("${com.cosmetic.upload.path}")
	private String uploadPath;
	
	// 상품리스트 Cart버튼을 누르면 장바구니에 상품이 저장됨
	// 장바구니테이블에 상품코드, 수량, 아이디
	@PostMapping("/cart_add")
	public ResponseEntity<String> cart_add(CartVO vo, HttpSession session) throws Exception {
		
		ResponseEntity<String> entity = null;
		
		// 로그인 사용자아이디
		String m_id = ((MemberVO)session.getAttribute("login_auth")).getM_id();
		vo.setM_id(m_id);//cartMapper.sql문 값 저장........
		
		cartService.cart_add(vo);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	@GetMapping("/cart_list")
	public void cart_list(HttpSession session, Model model) throws Exception {
		
		String m_id = ((MemberVO)session.getAttribute("login_auth")).getM_id();
		
		List<Map<String, Object>> cart_list = cartService.cart_list(m_id);
		
		// 날짜폴더의 역슬래쉬 \ 를 / 로 변환작업
		cart_list.forEach(cartVO -> {
			cartVO.put("pro_up_folder", cartVO.get("pro_up_folder").toString().replace("\\", "/"));
			
		});
		
		model.addAttribute("cart_list", cart_list);
	
		// 장바구니 비우기작업에서 총금액이 null로 발생됨. 타임리프에서 null 체크작업 필요
		model.addAttribute("cart_total_price", cartService.getCartTotalPriceByUserId(m_id));

	}
	
	// 이미지 출력
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		return fileUtils.getFile(uploadPath + "\\" + dateFolderName, fileName);
	}
	
	// 장바구니 비우기
	@GetMapping("/cart_empty")
	public String cart_empty(HttpSession session) throws Exception {
		
		String m_id = ((MemberVO)session.getAttribute("login_auth")).getM_id();
		
		cartService.cart_empty(m_id);
		
		return "redirect:/cart/cart_list";
				
	}
	
	// 상품삭제하는 기능.  보여주는 기능은 @GetMapping을 사용해야하고 @ModelAttribute 이럴때 사용
	@PostMapping("/cart_sel_delete")
	public String cart_sel_delte(int[] check, HttpSession session) throws Exception {
		// 배열인이유 : 같은 이름으로 name = check으로 넘어감 pro_num으
		// name은 중복가능
		String m_id = ((MemberVO)session.getAttribute("login_auth")).getM_id();
		
		cartService.cart_sel_delete(check,m_id);
		
		return "redirect:/cart/cart_list";
	}
	
	@GetMapping("/cart_change")
	public String cart_change(CartVO vo, HttpSession session) throws Exception{
		
		String m_id = ((MemberVO)session.getAttribute("login_auth")).getM_id();
		vo.setM_id(m_id);
		
		cartService.cart_change(vo);
		
		return "redirect:/cart/cart_list";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
