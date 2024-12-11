package com.cosmetic.shop.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cosmetic.shop.mail.EmailDTO;
import com.cosmetic.shop.mail.EmailService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RequestMapping("/member/*")// 공통주소
@Slf4j
@Controller
public class MemberController {
	
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	private final MemberService memberService;
	
	// 회원가입 폼
	@GetMapping("/join")  //  /member/join.html
	public void join() {
				
	}
		
	// 아이디 중복체크
	@GetMapping("/idCheck")
	public ResponseEntity<String> idCheck(String m_id) throws Exception {
					
		ResponseEntity<String> entity = null;
					
		String isUse = "";
					
		if(memberService.idCheck(m_id) != null) {
				isUse = "no"; // 아이디 사용불가능
		}else {
				isUse = "yes"; // 아이디 사용가능
		}
					
		entity = new ResponseEntity<String>(isUse, HttpStatus.OK);
					
		return entity;
	}
	
	// 회원정보 저장
	@PostMapping("/join")
	public String join(MemberVO vo) {// 데이터를 MemberVO로 받겠다. 그럼 join.html에서도 UserInfo 네임과 같아야 한다
		
		//log.info("회원정보 비밀번호 암호화 전 : " + vo);
		
		// passwordEncoder.encode(vo.getU_pw()) : 비밀번호를 암호화
		vo.setM_password(passwordEncoder.encode(vo.getM_password()));//암호화하고 세터로 저장
		
		//log.info("회원정보 비밀번호 암호화 후: " + vo);
		
		
		// db에 저장.
		//userInfoService.join(vo);
		memberService.join(vo);
		
		return "redirect:/member/login";
	}
	
	// 로그인 폼
	@GetMapping("/login")
	public void loginForm() {
		
	}
	
	// 로그인 처리.
	@PostMapping("/login")  // loginProcess(String u_id, String u_pw, HttpSession session)
	public String loginProcess(LoginDTO dto, HttpSession session, RedirectAttributes rttr) throws Exception {
		
		// 작업? 아이디와비번이 정상적이면, 세션객체로 인증작업을 처리하고, 메인페이지로 이동시킨다.
		// 아이디 또는 비번이 틀린 경우이면, 다시 로그인페이지로 이동시킨다.
		
		// memberVO가 null인지 여부를 체크
		// null이면 아이디가 존재안한다. null 아니면 아이디가 존재한다는 의미.
		MemberVO memberVO = memberService.login(dto.getM_id());
		
		String url = "";
		String status = "";
		if(memberVO != null) { // 아이디가 존재  matches("사용자가 입력비밀번호", "db에서 가져온 암호된비밀번호")
			// 사용자가 입력한 비밀번호가 db에서 가져온 암호화된 비밀번호를 만든것인지 확인
			if(passwordEncoder.matches(dto.getM_password(), memberVO.getM_password())) { // 비번이 맞는의미
				// 사용자를 인증처리하기위한 정보
				// UserInfo클래스인 userInfo객체가 Object형으로 저장된다. 꺼내올 때는 원래의 형(UserInfo클래스)으로 형변환시켜야 한다.
				session.setAttribute("login_auth", memberVO);
				
				url = "/";
			}else { // 비번이 틀린의미.
				status = "pwFail";
				url = "/member/login";
			}
		}else {  // 아이디가 존재 안한다.
			status = "idFail";
			url = "/member/login";
		}
		
		if(session.getAttribute("targetUrl") != null) { // 이전주소가 존재하면
			url = (String) session.getAttribute("targetUrl");
		}
		
		// 이동되는 주소의 타임리프페이지에서 status 이름으로 사용할수가 있다. 페이지에서 자바스크립트 문법으로 사용
		rttr.addFlashAttribute("status", status);
		
		
		return "redirect:"+ url;
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// session.setAttribute("login_auth", memberVO); 사용자를 인증처리하기위한 정보를 아래작업에서 소멸.
		session.invalidate(); // 서버측의 세션으로 저장된 모든메모리가 소멸.
		
		return "redirect:/";
	}
	
	// 클라이언트에서 데이타를 보낼때 사용한 파라미터명으로 컨트롤러 메서드에서는 동일하게 사용해야 한다.(규칙)
	// 메서드의 리턴타입이 void인 경우는 매핑주소가 파일명이된다. member/mypage.html
	// 리턴타입이 String 일때는 return "문자열" 이 파일명이된다.
	
	// 마이페이지
	@GetMapping("/mypage") 
	public void mypage() throws Exception {
		
	}
	
	//비밀번호 변경하기 폼
	@GetMapping("/pwchange") //  /member/pwchange.html
	public void pwchange() throws Exception {
		
	}
	
	//비밀번호 변경하기   <form><button type="submit" class="btn btn-primary">비밀번호 변경하기</button></form>
	@PostMapping("/pwchange")
	public String pwchange(@RequestParam("cur_pw") String mbsp_password, String new_pw, 
											HttpSession session, RedirectAttributes rttr) throws Exception {
		
		// RedirectAttributes rttr 기능?
		/*
		 1)주소를 리다이렉트 할때 파라미터를 이용하여, 쿼리스트링을 주소에 추가 할수가 있다.
		 2)리다이렉트 되는 주소의 타임리프 페이지에서 자바스크립트로 참조하고자 할 경우.  
		 */
		
		String url = "";
		String msg = "";
		
		// 암호화된 비밀번호.
		String db_mbsp_password = ((MemberVO) session.getAttribute("login_auth")).getM_password();
		String mbsp_id = ((MemberVO) session.getAttribute("login_auth")).getM_id();
		String mbsp_email = ((MemberVO) session.getAttribute("login_auth")).getM_email();
		
		// 현재 사용중인 비밀번호를 세션으로 저장되어있던 암호화된 비밀번호를 가져와서, 사용중인 비밀번호로 암호화가 된 것인지 여부를 판단.
		if(passwordEncoder.matches(mbsp_password, db_mbsp_password)) {
						
			// 1)신규비밀번호를 암호화한다. 60바이트로 암호화
			String encode_new_pw = passwordEncoder.encode(new_pw);
			
			// 2)암호화한 비밀번호를 변경한다.
			memberService.pwchange(mbsp_id, encode_new_pw);
			
			url = "/"; // 메인페이지로 이동
			msg = "success";
			
			// 비밀번호 변경알림 메일발송
			String type = "mail/pwchange";
			
			EmailDTO dto = new EmailDTO();
			dto.setReceiverMail(mbsp_email); // 받는사람 메일주소
			dto.setSubject("Ezen Mall 비밀번호 변경알림을 보냅니다.");
			
			emailService.sendMail(type, dto, new_pw);
			
		}else {
			url = "/member/pwchange"; // 비밀번호 변경 폼주소로 이동
			msg = "fail";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:" + url;
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
	
}
