package com.cosmetic.shop.mail;

import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // 생성자 생성이 된다. 의존성주입
@Service
public class EmailService {

	private final JavaMailSender mailSender; //G-Mail SMTP 메일서버정보를 가지고 있다./JavaMailSender가 mailSender에 의존성 주입됨
	
	// 메일 내용을 보낼때 타임리프페이지가 실행되어 html code를 사용하고 싶은 경우.
	private final SpringTemplateEngine templateEngine;

	// String type : 메일발송 용도(인증코드, 회원가입축하, 주문내역정보 등등)
	// type 매개변수에 타임리프 파일명이 제공
	// String message : 용도는 메일내용
	//  "mail/idsearch"
	public void sendMail(String type, EmailDTO dto, String message) {
		
		// MimeMessage : 메일구성정보를 관리(받는사람, 보내는사람, 받는사람 메일주소, 본문내용)
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		try {
			
		
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
				
				// 메일 받는 주소
				mimeMessageHelper.setTo(dto.getReceiverMail());
				// 메일 보내는 주소및이름 
				mimeMessageHelper.setFrom(new InternetAddress(dto.getSenderMail(), dto.getSenderName()));
				// 메일 제목
				mimeMessageHelper.setSubject(dto.getSubject());
				
				// 메일 본문내용.  true : 보내는 내용이 html tag가 존재할 경우 태그효과
				// type: "mail/idsearch" idsearch.html
				mimeMessageHelper.setText(setContext(message, type), true);
				
				// 메일발송기능
				mailSender.send(mimeMessage);//실직적으로 메일 발송됨
		}catch(Exception ex) { // 메일발송시 에러발생되면, catch가 동작된다.
			ex.printStackTrace();
		}
		
	}
	
	// 인증코드 생성(숫자, 영문대소문자를 이용하여 8개의 문자추출)
	public String createAuthCode() {
		Random random = new Random(); // 무작위 작업.
		StringBuffer key = new StringBuffer();
		
		for(int i=0; i<8; i++) {
			int index = random.nextInt(4);
			
			switch(index) {
				case 0: key.append((char) ((int) random.nextInt(26) + 97)); break; // 영문 소문자
				case 1: key.append((char) ((int) random.nextInt(26) + 65)); break; // 영문 대문자
				default: key.append(random.nextInt(9)); // 숫자(0~8)
			}
		}
		
		return key.toString();
	}
	
	// 아이디찾기기능이라면  idsearch.html 파일에 찾은 아이디와 내용을 결합하여 메일로 보내는 내용을 만들어준자. 
	// 메일 템플릿사용(thymeleaf 사용).  user01   mail/idsearch
	public String setContext(String message, String type) {
		Context context = new Context();
		
		// 타임리프페이지 "authcode" 이름으로 데이타를 전달
		context.setVariable("message", message);
		// type : "authcode" -> authcode.html
		return templateEngine.process(type, context); // 서버에서 타임리프 파일이 실행되어 html code결과가 생성된다.
	}
	
	
	
	
	
	
	
	
	
	
}
