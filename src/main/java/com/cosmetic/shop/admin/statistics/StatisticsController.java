package com.cosmetic.shop.admin.statistics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/admin/statist/*")
@RequiredArgsConstructor
@Controller
public class StatisticsController {
	
	private final StatisticsService statisticsService;
	
	// 전체주문통계
	@GetMapping("/static_sale_all")
	public void static_sale_all() throws Exception {
				
	}
	
	

}
