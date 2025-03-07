package com.cosmetic.shop.admin.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.cosmetic.shop.common.utils.SearchCriteria;
import com.cosmetic.shop.review.ReviewReply;
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
	
	public int review_count(@Param("cri") SearchCriteria cri, @Param("rev_rate") String rev_rate,
			@Param("rev_content") String rev_content) {
		return adReviewMapper.review_count(cri, rev_rate, rev_content);
	}
	
	public void reply_insert(ReviewReply vo) {
		adReviewMapper.reply_insert(vo);
	}
	
	public ReviewReply reply_info(Long reply_id) {
		return	adReviewMapper.reply_info(reply_id);
	}
	
	public void reply_modify(Long reply_id, String reply_text) {
		adReviewMapper.reply_modify(reply_id, reply_text);
	}
	
	public void reply_delete(Long reply_id) {
		adReviewMapper.reply_delete(reply_id);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
