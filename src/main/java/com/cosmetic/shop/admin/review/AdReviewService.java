package com.cosmetic.shop.admin.review;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cosmetic.shop.common.utils.SearchCriteria;
import com.cosmetic.shop.review.ReviewVO;

import lombok.RequiredArgsConstructor;
//비지니스 로직작업 목적
@Service
@RequiredArgsConstructor
public class AdReviewService {
	
	public final AdReviewMapper adReviewMapper;
	
	public List<ReviewVO> review_list(SearchCriteria cri,
							String rev_rate, String rev_content){
		return adReviewMapper.review_list(cri, rev_rate, rev_content);
		
	}

}
