package com.cosmetic.shop.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CartMapper {
	
	void cart_add(CartVO vo);
	
	List<Map<String, Object>> cart_list(String m_id);
	
	Integer getCartTotalPriceByUserId(String m_id);
	
	void cart_empty(String m_id);
	
	void cart_sel_delete(HashMap<String, Object> map);
	
	void cart_change(CartVO vo);
	
	
	
	
	

}
