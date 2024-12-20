package com.cosmetic.shop.admin.product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cosmetic.shop.admin.category.AdCategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//관리자 : 상품관리기능
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product/*")
public class AdProductController {

	private final AdProductService adProductService;
	private final AdCategoryService adCategoryService;

	// 상품등록 폼.  1차카테고리 정보를 출력.	
	@GetMapping("/pro_insert")
	public void pro_insert(Model model) {
		
		// 1차카테고리 목록
		model.addAttribute("cate_list", adCategoryService.getFirstCategoryList());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
