package com.cosmetic.shop.admin.product;

import java.util.List;

import com.cosmetic.shop.common.utils.SearchCriteria;

public interface AdProductMapper {

	// insert,update,delete 용도로 사용하는 메서드는 리턴타입이 void
	// select용도 사용하는 메서드는 리턴타입이 그 결과에 맞는 타입을 사용
	// 상품등록작업
	void pro_insert(ProductVO vo);
	
	List<ProductVO> pro_list(SearchCriteria cri);
	
	int getTotalCount(SearchCriteria cri);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
