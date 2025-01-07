package com.cosmetic.shop.cart;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CartVO {

	private Integer pro_num;
	private String m_id;
	private int cart_amount;
	private LocalDateTime cart_date;
	
}
