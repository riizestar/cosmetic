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
	
	public int getTotalCount(SearchCriteria cri) {
		return adProductMapper.getTotalCount(cri);
	}
	
	public ProductVO pro_edit_form(Integer pro_num) {
		return adProductMapper.pro_edit_form(pro_num);
	}
	
	public void pro_edit_ok(ProductVO vo) {
		adProductMapper.pro_edit_ok(vo);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
