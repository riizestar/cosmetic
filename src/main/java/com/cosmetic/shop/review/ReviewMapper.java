package com.cosmetic.shop.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cosmetic.shop.common.utils.SearchCriteria;

public interface ReviewMapper {
	
	// 1)상품후기 테이블 1개로 쿼리를 조회
	List<ReviewVO> rev_list(@Param("pro_num") Integer pro_num, 
							@Param("cri") SearchCriteria cri);
	
	// 페이징정보를 구성하기 위한 상품후기 개수.
	int getCountReviewByPro_num(Integer pro_num);
	
	void review_save(ReviewVO vo);
	
	// 수정목적으로 사용할 상품후기정보
	ReviewVO review_info(Long rev_code); 
	
	void review_modify(ReviewVO vo);
	
	void review_delete(Long rev_code);

}
