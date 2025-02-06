package com.cosmetic.shop.review;

import java.time.LocalDateTime;

import com.cosmetic.shop.admin.product.ProductVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewVO {
	
	// 상품후기
	private Long rev_code;
	private String m_id;
	private Integer pro_num;
	private String rev_content;
	private int rev_rate;
	private LocalDateTime rev_date;
	
	// 상품
	// 사용자 상품후기목록에서는 사용 안함.
	// 관리자 상품후기목록에서는 사용 함.
	private ProductVO product;
	
	

}
