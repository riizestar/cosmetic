package com.cosmetic.shop.cart;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class CartService {
	
	private final CartMapper cartMapper;
	
	public void cart_add(CartVO vo) {
		cartMapper.cart_add(vo);
	}

}
