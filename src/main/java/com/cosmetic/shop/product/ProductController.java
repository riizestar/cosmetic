package com.cosmetic.shop.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cosmetic.shop.admin.category.AdCategoryService;
import com.cosmetic.shop.common.utils.FileUtils;
import com.cosmetic.shop.common.utils.PageMaker;
import com.cosmetic.shop.common.utils.SearchCriteria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor 
@RequestMapping("/product/*")
@Controller
public class ProductController {

	private final ProductService productService;
	private final AdCategoryService adCategoryService;
	
	// 상품이미지 관련작업기능
	private final FileUtils fileUtils;
	
	@Value("${com.ezen.upload.path}")
	private String uploadPath;
	
	@Value("${com.ezen.upload.ckeditor.path}")
	private String uploadCKPath;
	
	// 2차 카테고리에 따른 상품리스트는 페이징 사용, 검색기능은 미사용
	@GetMapping("/pro_list")
	public void pro_list(SearchCriteria cri,@ModelAttribute("cate_name") String cate_name,
			Integer cate_code, Model model) throws Exception {
		
		log.info("카테고리명: " + cate_name);
		//1차 카테고리목록
		model.addAttribute("cate_list", adCategoryService.getFirstCategoryList());
		
		//2차 카테고리의 상품목록
		model.addAttribute("pro_list", productService.getProductListBysecondCategory(cri, cate_code));
		
		// 페이징작업
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		
		pageMaker.setTotalCount(productService.getCountProductListBysecondCategory(cate_code));
		
		model.addAttribute("pageMaker", pageMaker);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
