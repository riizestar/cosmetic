package com.cosmetic.shop.admin;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//관리자 로그인/로그아웃, 관리메뉴
@Slf4j
@RequiredArgsConstructor// final 주입
@RequestMapping("/admin/*")
@Controller
public class AdminController {

	private final AdminService adminService;
	private final PasswordEncoder passwordEncoder;
	
	//로그인 주소
	@GetMapping("/")
	public String admin_login() {
		
		return "/admin/adLogin";
	}
	
	// 관리자 로그인 처리
	@PostMapping("/admin_ok")
	public String admin_ok(AdminDto dto, HttpSession session, RedirectAttributes rttr) throws Exception {
		
		AdminDto db_vo = adminService.admin_ok(dto.getAd_userid());
		
		String url = "";
		String msg = "";//RedirectAttributes rttr
		if(db_vo != null) {// 아이디가 존재  matches("사용자가 입력비밀번호", "db에서 가져온 암호된비밀번호")
			// 사용자가 입력한 비번이 디비에서 가져온 암호화된 비번을 만드는데 사용한 것인지 체크 : matches
			if(passwordEncoder.matches(dto.getAd_passwd(), db_vo.getAd_passwd())) {// 비번이 맞는의미
				// 사용자를 인증처리하기 위한 정보
				// UserInfo클래스인 userInfo객체가 오브젝형으로 저장된다. 꺼내올 떄는 원래의 형으로 (유저인포클래스)로 형변환시켜야 함
				session.setAttribute("admin_auth", db_vo);// String name - , Object value - userInfo// "login_auth" 아이디가 필요하면 HttpSession session 만들어야한다
				// 세션에 저장    / 아이디와 비번이 일치하면 정보를 admin_auth이름으로 저장,관리함/ admin_auth 이름은 중복되면 안됨
				session.setAttribute("admin_auth", db_vo);
				url = "/admin/ad_menu";
			}else {// 비번이 틀린의미.
				url = "/admin/";
				msg = "pwfail";
			}
		}else {// 아이디가 존재 안한다.
			url = "/admin/";
			msg = "idfail";
		}
		
		rttr.addFlashAttribute("msg", msg); // 타임리프파일에서 자바스크립트로 참조
		
		return "redirect:" + url;
	}
	
	@GetMapping("/ad_menu")
	public String menu() {
		
		return "/admin/ad_menu";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
