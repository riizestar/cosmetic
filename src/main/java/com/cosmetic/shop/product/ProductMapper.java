package com.cosmetic.shop.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cosmetic.shop.admin.product.ProductVO;
import com.cosmetic.shop.common.utils.SearchCriteria;

public interface ProductMapper {
	
	List<ProductVO> getProductListBysecondCategory(@Param("cri") SearchCriteria cri,
			@Param("cate_code") Integer second_cate_code);
	
	int getCountProductListBysecondCategory(@Param("cate_code") Integer second_cate_code);
	
	ProductVO pro_info(Integer pro_num);
	
	// 상품테이블 후기카운트 증가작업
	void review_count(Integer pro_num);
	
	// 상품후기 작업후 리뷰카운트 조회.
	int review_count_pro_info(Integer pro_num);
	
	
	
	
	
	

}
