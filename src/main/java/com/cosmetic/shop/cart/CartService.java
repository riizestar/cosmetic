package com.cosmetic.shop.cart;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class CartService {
	
	private final CartMapper cartMapper;
	
	public void cart_add(CartVO vo) {
		cartMapper.cart_add(vo);
	}
	
	public List<Map<String, Object>> cart_list(String m_id){
		return cartMapper.cart_list(m_id);
	}
	
	public Integer getCartTotalPriceByUserId(String m_id) {
		return cartMapper.getCartTotalPriceByUserId(m_id);
	}
	
	public void cart_empty(String m_id) {
		cartMapper.cart_empty(m_id);
	}

}
