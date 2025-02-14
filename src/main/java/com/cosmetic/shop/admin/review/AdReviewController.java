package com.cosmetic.shop.admin.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cosmetic.shop.common.utils.FileUtils;
import com.cosmetic.shop.common.utils.SearchCriteria;
import com.cosmetic.shop.review.ReviewService;
import com.cosmetic.shop.review.ReviewVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/review/*")
@RequiredArgsConstructor
public class AdReviewController {
	
	public final AdReviewService adReviewService;
	public final ReviewService reviewService;
	
	// 상품이미지 관련작업기능
	public final FileUtils fileUtils;
	
	@Value("${com.ezen.upload.path}")
	private String uploadPath;
	
	// 주소를 호출시 쿼리스트링이 존재하지 않으면 SearchCriteria cri 기본생성자가 호출된다.  page=1, perPagenum=10
	@GetMapping("/review_list")
	public void review_list(@ModelAttribute("cri") SearchCriteria cri, 
							@ModelAttribute("rev_rate") String rev_rate,
							@ModelAttribute("rev_content") String rev_content) throws Exception {
		
		log.info("검색정보" + cri.toString());
		
		List<ReviewVO> review_list = adReviewService.review_list(cri, rev_rate, rev_content);
		
		// 날짜폴더의 역슬래시를 슬래시로 변환하는 작업.
		review_list.forEach(reivew_Info -> {
			reivew_Info.getProduct().setPro_up_folder((reivew_Info.getProduct().getPro_up_folder().replace("\\", "/")));
		});
		
		// 페이지 메이커 작업해야 함
				
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
