package com.cosmetic.shop.cart;

import java.util.HashMap;
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
	
	public void cart_sel_delete(int[] pro_num,String m_id) {
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("pro_num_arr", pro_num);//("키",값) 선택된 상품코드
		map.put("m_id", m_id);// 사용자 아이디
		
		cartMapper.cart_sel_delete(map);
		
	}
	
	public void cart_change(CartVO vo) {
		cartMapper.cart_change(vo);
	}

}
