package com.cosmetic.shop.kakaopay;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
//결제준비(ready) 요청 파라미터
@Getter // private 필드를 대상으로 getter메서드 생성
@AllArgsConstructor // 모든 필드를 대상으로 생성자메서드 생성
@Builder  // 객체가 생성되었을 때, 객체가 가리키는 필드의 기억장소를 선택적으로 사용이 가능하게 해주는 어노테이션
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReadyRequest { // 클래스명은 다르게 임의적으로 만들어도 상관이 없지만, 의미에 맞게 만들다보니 이 이름으로 생성하게 됨.

	// 필드(속성)이름은 https://developers.kakaopay.com/docs/payment/online/single-payment 준비요청에서 정한 이름으로 구성해야 한다.
	private String cid;
	private String partner_order_id;
	private String partner_user_id;
	private String item_name;
	private int quantity;
	private int total_amount;
	private int tax_free_amount;
	private String approval_url;
	private String cancel_url;
	private String fail_url;
	

	/* @Builder 어너테이션 기능으로 다양한 매개변수를 이용한 생성자를 정의를 하지않고 객체가 가리키는 기억장소에 값을 저장할수가 있다.
	public ReadyRequest(String cid) {
		this.cid = cid;
	}
	
	public ReadyRequest(String cid, String partner_order_id) {
		this.cid = cid;
		this.partner_order_id = partner_order_id;
	}
	*/

}
