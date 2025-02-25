package com.cosmetic.shop.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cosmetic.shop.admin.category.AdCategoryService;
import com.cosmetic.shop.admin.product.ProductVO;
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
	
	@Value("${com.cosmetic.upload.path}")
	private String uploadPath;
	
	@Value("${com.cosmetic.upload.ckeditor.path}")
	private String uploadCKPath;
	
	// 2차 카테고리에 따른 상품리스트는 페이징 사용, 검색기능은 미사용
	@GetMapping("/pro_list")
	public void pro_list(SearchCriteria cri,@ModelAttribute("cate_name") String cate_name,
			Integer cate_code, Model model) throws Exception {
		
		log.info("카테고리명: " + cate_name);
		//1차 카테고리목록
		model.addAttribute("cate_list", adCategoryService.getFirstCategoryList());
		
		// 날짜폴더의 \를 /로 변환하는 작업
		List<ProductVO> pro_list = productService.getProductListBysecondCategory(cri, cate_code);
		
		pro_list.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));
		});
		
		//2차 카테고리의 상품목록
		model.addAttribute("pro_list", pro_list);
		
		// 페이징작업
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		
		pageMaker.setTotalCount(productService.getCountProductListBysecondCategory(cate_code));
		
		model.addAttribute("pageMaker", pageMaker);
	}
	
	// 상품목록 이미지출력.. 클라이언트에서 보낸 파라미터명 스프링의 컨트롤러에서 받는 파라미터명이 일치해야 한다
	@GetMapping("/image_display")
	public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
		
		return fileUtils.getFile(uploadPath + "\\" + dateFolderName, fileName);
	}
	
	// 상품상세정보
	@GetMapping("/pro_info")
	public void pro_info(@ModelAttribute("cate_name") String cate_name, Integer pro_num, Model model) throws Exception {
		
		log.info("카테고리명: " + cate_name);
		
		// 상품정보
		ProductVO productVO = productService.pro_info(pro_num);
		
		
		log.info("상품정보: " + productVO);
		
		// 이미지파일의 날짜폴더 \를 /로 변환하는 작업
		productVO.setPro_up_folder(productVO.getPro_up_folder().replace("\\", "/"));
		
		model.addAttribute("productVO", productVO);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
