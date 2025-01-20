package com.cosmetic.shop.delivery;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DeliveryVO {
	
	private Long delivery_id;
	private Integer ord_code;
	private String shipping_zipcode;
	private String shipping_addr;
	private String shipping_deaddr;
	private LocalDateTime delivery_date;
	private String delivery_status;
	
	

}
