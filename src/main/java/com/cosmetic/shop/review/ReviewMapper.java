package com.cosmetic.shop.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cosmetic.shop.common.utils.SearchCriteria;

public interface ReviewMapper {
	
	// 1)상품후기 테이블 1개로 쿼리를 조회
	List<ReviewVO> rev_list(@Param("pro_num") Integer pro_num, 
							@Param("cri") SearchCriteria cri);
	
	void review_save(ReviewVO vo);

}
