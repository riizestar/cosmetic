package com.cosmetic.shop.review;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cosmetic.shop.common.utils.SearchCriteria;
import com.cosmetic.shop.product.ProductMapper;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class ReviewService {

	private final ReviewMapper reviewMapper;
	private final ProductMapper productMapper;
	
	public List<ReviewVO> rev_list(Integer pro_num, SearchCriteria cri){
		
		return reviewMapper.rev_list(pro_num, cri);
	}
	
	public int getCountReviewByPro_num(Integer pro_num) {
		return reviewMapper.getCountReviewByPro_num(pro_num);
	}
	
	@Transactional
	public void review_save(ReviewVO vo) {
		// 1)상품후기 등록
		reviewMapper.review_save(vo);
		
		// 2)상품테이블 후기카운트 증가작업(증가만하고 다시 읽어오는 작업은 review_count_pro_info())
		productMapper.review_count(vo.getPro_num());
	}
	
	public ReviewVO review_info(Long rev_code) {
		return reviewMapper.review_info(rev_code);
	}
	
	public void review_modify(ReviewVO vo) {
		reviewMapper.review_modify(vo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
