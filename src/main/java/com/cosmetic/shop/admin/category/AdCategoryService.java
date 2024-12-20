package com.cosmetic.shop.admin.category;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdCategoryService {
	
	private final AdCategoryMapper adCategoryMapper;
	
	public List<CategoryVO> getFirstCategoryList() {
		return adCategoryMapper.getFirstCategoryList();
	}
	
	public List<CategoryVO> getSecondCategoryList(Integer cate_prt_code) {
		return adCategoryMapper.getSecondCategoryList(cate_prt_code);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
