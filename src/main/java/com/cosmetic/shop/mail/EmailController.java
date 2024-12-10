package com.cosmetic.shop.mail;
// 이메일
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor //필드가 final이거나 @NonNull**인 필드에 대해서 생성자를 자동으로 생성
@RestController  // @Controller + @ResponseBody // REST API 쓸려고 사용함
@RequestMapping("/email/*")
public class EmailController {

	private final EmailService emailService;
	
	// 메일 인증코드발급
	@GetMapping("/authcode")
	public ResponseEntity<String> authcode(String type, EmailDTO dto, HttpSession session) {
		
		log.info("메일정보: " + dto);
		
		ResponseEntity<String> entity = null;
		
		// type : 메일 템플릿파일명.  "authcode" -> authcode.html
		type = "mail/" + type;
		
		String authcode =  emailService.createAuthCode(); 
		
		// 메일인증코드를 세션에 저장
		session.setAttribute("authcode", authcode);// 메일인증메세지랑 맞는지 확인.....서버측에 저장한 메모리는 session.setAttribute
		
		emailService.sendMail(type, dto, authcode);// type = "mail/" + type; authcode.html을 가르킴/ 실제로 메일을 보내는 기능
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}
	
	// 인증코드 확인
	@GetMapping("/confirm_authcode")
	public ResponseEntity<String> confirm_authcode(String authcode, HttpSession session) {
		
		ResponseEntity<String> entity = null;
		// 인증확인을 위하여 서버측에 저장했떤 인증코드를 읽어오는 작업
		String au_code = (String)session.getAttribute("authcode");// 읽어왔던 형으로 불러온다. 스트링으로 저장했으니까 형변환해서 스트롱으로 갖고온다. setAttribute에 오브젝으로 저장했으니까
		
		String result = "";
		
		// 사용자가 입력한 인증코드와 세션으로 저장했돈 발급해준 인증코드를 비교
		if(authcode.equals(au_code)) { //세션이름은 authcode
			result = "success";
			// 세션제거 (서버측의 사용한 메모리소멸)
			session.removeAttribute("authcode");
			
		}else {
			result = "fail";
		}
		
		entity = new ResponseEntity<String>(result, HttpStatus.OK);
	
		return entity;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
