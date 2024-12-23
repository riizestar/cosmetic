package com.cosmetic.shop.admin.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cosmetic.shop.common.utils.SearchCriteria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdProductService {
	
	private final AdProductMapper adProductMapper;
	
	public void pro_insert(ProductVO vo) {
		adProductMapper.pro_insert(vo);
	}
	
	public List<ProductVO> pro_list(SearchCriteria cri) {
		return adProductMapper.pro_list(cri);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
