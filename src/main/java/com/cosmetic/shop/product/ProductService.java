package com.cosmetic.shop.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.cosmetic.shop.admin.product.ProductVO;
import com.cosmetic.shop.common.utils.SearchCriteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductMapper productMapper;
	
	public List<ProductVO> getProductListBysecondCategory(SearchCriteria cri, Integer second_cate_code){
		return productMapper.getProductListBysecondCategory(cri, second_cate_code);
	}
	
	public int getCountProductListBysecondCategory(@Param("cate_code") Integer second_cate_code) {
		return productMapper.getCountProductListBysecondCategory(second_cate_code);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}