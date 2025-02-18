package com.cosmetic.shop.admin.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cosmetic.shop.common.utils.SearchCriteria;
import com.cosmetic.shop.review.ReviewReply;
import com.cosmetic.shop.review.ReviewVO;

public interface AdReviewMapper {
	
	List<ReviewVO> review_list(@Param("cri") SearchCriteria cri,
			@Param("rev_rate") String rev_rate, @Param("rev_content") String rev_content);

	int review_count(@Param("cri") SearchCriteria cri, @Param("rev_rate") String rev_rate,
			@Param("rev_content") String rev_content);
	
	void reply_insert(ReviewReply vo);
	
	
	
	
}
