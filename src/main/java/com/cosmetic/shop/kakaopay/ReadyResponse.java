package com.cosmetic.shop.kakaopay;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 결제준비(ready) : 카카오페이 결제를 시작하기 위해 결제정
//ajax 호출한 쪽으로 사용
@Getter
@Setter
@ToString
public class ReadyResponse {

	private String tid;
	private String next_redirect_pc_url;
	private Date created_at;
	
}
