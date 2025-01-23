package com.cosmetic.shop.mail;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor // 생성자 생성이 된다. 의존성주입
@Service
public class EmailService {
	
	private static final int AUTH_CODE_LENGTH = 8;// 인증코드 8자리
	private static final String UTF_8_ENCODING = "UTF-8";//문자열을 인코딩할 때 사용되는 UTF-8 문자 인코딩 방식을 지정
	
	private final JavaMailSender mailSender; //G-Mail SMTP 메일서버정보를 가지고 있다./JavaMailSender가 mailSender에 의존성 주입됨
	
	// 메일 내용을 보낼때 타임리프페이지가 실행되어 html code를 사용하고 싶은 경우.
	private final SpringTemplateEngine templateEngine; // Thymeleaf 템플릿 엔진

	// String type : 메일발송 용도(인증코드, 회원가입축하, 주문내역정보 등등)
	// type 매개변수에 타임리프 파일명이 제공
	// String message : 용도는 메일내용
	//  "mail/idsearch"
    // 메일 발송 공통 메서드
    private void sendEmail(EmailDTO dto, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, UTF_8_ENCODING);

            // MimeMessage : 메일구성정보를 관리(받는사람, 보내는사람, 받는사람 메일주소, 본문내용)
            mimeMessageHelper.setTo(dto.getReceiverMail());// 메일 받는 주소
            mimeMessageHelper.setFrom(new InternetAddress(dto.getSenderMail(), dto.getSenderName()));// 메일 보내는 주소및이름
            mimeMessageHelper.setSubject(dto.getSubject());// 메일 제목
            mimeMessageHelper.setText(htmlContent, true);
            
         // 메일발송기능
            mailSender.send(mimeMessage); // G-Mail Server의 기능을 사용하여 메일이 전송된다.
        } catch (Exception ex) {
            log.error("메일 발송 실패: {}", ex.getMessage(), ex);
            throw new RuntimeException("메일 발송 중 오류가 발생했습니다.", ex);
        }
    }

    // 단순 메시지를 포함한 메일 발송
    public void sendMail(String type, EmailDTO dto, String message) {
        String htmlContent = generateHtmlContent(message, type);
        sendEmail(dto, htmlContent);
    }

    // 주문 내역을 포함한 메일 발송
    public void sendMail(String type, EmailDTO dto, List<Map<String, Object>> orderInfo, int orderTotalPrice) {
        String htmlContent = generateOrderHtmlContent(orderInfo, orderTotalPrice, type);
        sendEmail(dto, htmlContent);
    }

    // 인증 코드 및 임시 비밀번호 생성
    // 인증코드 생성(숫자, 영문대소문자를 이용하여 8개의 문자추출)
    public String createAuthCode() {
        Random random = new Random();// 무작위 작업.
        StringBuilder authCode = new StringBuilder(AUTH_CODE_LENGTH);

        for (int i = 0; i < AUTH_CODE_LENGTH; i++) {
            int randomType = random.nextInt(3);
            switch (randomType) {
                case 0 -> authCode.append((char) (random.nextInt(26) + 'a')); // 소문자
                case 1 -> authCode.append((char) (random.nextInt(26) + 'A')); // 대문자
                default -> authCode.append(random.nextInt(10)); // 숫자(0~8)
            }
        }
        return authCode.toString();
    }

    // Thymeleaf를 사용한 HTML 컨텐츠 생성 (일반 메시지)
    private String generateHtmlContent(String message, String templateName) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process(templateName, context);
    }

    // Thymeleaf를 사용한 HTML 컨텐츠 생성 (주문 내역)
    private String generateOrderHtmlContent(List<Map<String, Object>> orderInfo, int orderTotalPrice, String templateName) {
        
    	// 주문내역 html파일에 사용할 주문내역 데이타와 주문총금액 데이타 작업
    	Context context = new Context();
        context.setVariable("order_info", orderInfo); // Model 과 같은 의미로 해석한다.
        context.setVariable("order_total_price", orderTotalPrice);
        
        
        
        return templateEngine.process(templateName, context);  // 타임리프와 주문내역, 주문총금액의 내용이 합쳐져서, html code 가 생성
    }
	
	
	
	
	
	
	
	
	
	
}
