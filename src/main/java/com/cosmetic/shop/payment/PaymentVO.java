package com.cosmetic.shop.payment;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class PaymentVO {

	private Integer payment_id;
	private Integer ord_code;
	private String m_id;
	private String payment_method;
	private int payment_price;
	private String payment_status;
	private LocalDateTime payment_date;
	
	
	
}
