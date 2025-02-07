package com.cosmetic.shop.review;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewReply {
	
	private Long reply_id;// 상품후기 답변번호
    private Long rev_code;// 상품후기번호
    private String manager_id;// 관리자 아이디
    private String reply_text;// 후기 답변 내용
    private LocalDateTime reply_date;// 후기 답변 등록일

}
