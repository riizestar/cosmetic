package com.cosmetic.shop.review;

import java.util.List;

import org.springframework.stereotype.Service;

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
	
	
	public void review_save(ReviewVO vo) {
		// 1)상품후기 등록
		reviewMapper.review_save(vo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
