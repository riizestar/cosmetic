package com.cosmetic.shop.admin.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cosmetic.shop.common.utils.FileUtils;
import com.cosmetic.shop.common.utils.SearchCriteria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RequestMapping("/admin/order/*")
@Controller
@Slf4j
public class AdOrderController {
	
	private final AdOrderService adOrderService;
	
	// 상품이미지 관련작업 기능
	private final FileUtils fileUtils;
	
	@Value("${com.cosmetic.upload.path}")
	private String uploadPath;
	
	// 주문목록
	@GetMapping("/order_list")
	public void order_list(@ModelAttribute("cri") SearchCriteria cri,
			@ModelAttribute("period") String period,
			@ModelAttribute("start_date") String start_date,
			@ModelAttribute("end_date") String end_date,
			@ModelAttribute("payment_method") String payment_method,
			@ModelAttribute("ord_status") String ord_status, Model model) throws Exception{
		
	}
	
}
