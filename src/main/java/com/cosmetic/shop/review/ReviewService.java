package com.cosmetic.shop.review;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class ReviewService {

	private final ReviewMapper reviewMapper;
	
	public void review_save(ReviewVO vo) {
		// 1)상품후기 등록
		reviewMapper.review_save(vo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
