package com.cosmetic.shop.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EmailDTO {

	private String senderName; // 발신자이름
	private String senderMail; // 발신자 메일주소
	private String receiverMail;  // 수신자 메일주소
	private String subject;  // 제목
	private String message;  // 내용
	
	// 클래스를 정의하면, 기본생성자는 컴파일과정에서 자동으로 생성된다.
	// 기본생성자를 재정의하는 이유는 기억장소(필드)에 값을 사용자 정의하기 위해서이다.
	// 기본생성자 재정의
	public EmailDTO() {
		this.senderMail = "CosmeticShop";
		this.senderName = "CosmeticShop";
		this.subject = "Cosmetic Shop 회원가입 메일인증코드입니다.";
		this.message = "메일 인증코드를 확인하시고, 회원가입시 인증코드 입력란에 입력바랍니다.";
	}
}
