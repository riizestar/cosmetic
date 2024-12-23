package com.cosmetic.shop.admin.product;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdProductService {
	
	private final AdProductMapper adProductMapper;
	
	public void pro_insert(ProductVO vo) {
		adProductMapper.pro_insert(vo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
