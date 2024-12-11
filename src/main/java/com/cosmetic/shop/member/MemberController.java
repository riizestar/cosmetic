package com.cosmetic.shop.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
	
}
