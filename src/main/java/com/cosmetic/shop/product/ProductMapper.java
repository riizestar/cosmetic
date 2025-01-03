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
	
	
	
	
	
	

}
