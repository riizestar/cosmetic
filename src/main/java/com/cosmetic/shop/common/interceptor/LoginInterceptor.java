package com.cosmetic.shop.common.interceptor;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.cosmetic.shop.member.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
//인터셉터기능을 갖는 클래스는 HandlerInterceptor 인터페이스를 상속받아 구현해야 한다.
@Component
public class LoginInterceptor implements HandlerInterceptor {

	// 인터셉터 클래스가 관리하는 URI 요청이 발생되면, 인터셉터가 가로채서 preHandle()메서드가 먼저 동작(실행)
	// HttpServletRequest request : 클라이언트가 요청한(보내온) 모든 정보(데이타)를 서버에서 관리하는 객체.
	// HttpServletResponse response : 서버에서 클라이언트로 보내는 응답정보를 가지고 있는 객체.
		
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		boolean result = false;
		
		// 로그인 session.setAttribute("login_auth", userInfo);
		// 인증된 상태인지 체크하는 작업
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("login_auth");
		
		if(memberVO == null) { // 현재 주소를 요청한 사용자는(브라우저) 로그인을 하지않은 의미.(인증을 안한 상태)
			result = false;
			
			// ajax요청주소인지 구분하는 작업.
			if(isAjaxRequest(request)) {
				
				String originalUrl = request.getRequestURI();
				String postData = getPostData(request);//"username=abc&password=123"
				
				System.out.println("데이타: " + postData);
				
				// 로그인하지 않은 사용자가 요청한 URL을 세션에 저장. 나중에 사용자가 로그인 후 해당 URL로 리다이렉트할 수 있도록
				session.setAttribute("targetUrl", originalUrl);
				// POST 요청으로 보낸 데이터를 세션에 저장. 로그인 후에 다시 같은 데이터를 보낼
				session.setAttribute("postData", postData);
				
				// response.sendRedirect("/member/login?targetUrl=" + URLEncoder.encode(originalUrl, "UTF-8"));
				
				// ajax호출한 쪽으로 제어가 넘어간다. error 구문으로
				response.sendError(400);//400 Http상태코드. ajax로 제어가 400번 클라이언트 에러정보를 가지고 넘어간다.
			}else {
				getTargetUrl(request);// 원래요청된 주소를 세션형태로 저장  "targetUrl". 즉 인증되지 않은 상태에서 인증된 사용만 사용하는 주소를 요청했을  그 주소를 저장하는 메서드
			
				response.sendRedirect("/member/login");// 로그인하지 않은 사용자가 요청한 경우 로그인 페이지로 리다이렉트
			}
		}else {// 로그인을 한 의미.(인증을 한 상태)
			result = true; // true이면, 컨트롤러로 실행이 넘어간다.
		}
	
		return result;
	}

	// 클라이언트의 요청이 ajax인지 체크하는 기능
	private boolean isAjaxRequest(HttpServletRequest request) {
		
		boolean isAjax = false;
		
		// ajax 작업하는 자바스크립트 쪽에서 AJAX헤더작업을 추가해야 한다.
		String header = request.getHeader("AJAX");// 헤더에 "AJAX"라는 키, "true"는 값/ "AJAX" 헤더를 보내지 않았다면, header는 null
		
		if(header != null && header.equals("true")){// 헤더가 널이 아니고 값이 "true" 일치하면
			isAjax = true;
		}
			return isAjax;
			
	}
	
	// 인증되지 않은 상태에서 ajax방식으로 post 요청시 사용한 데이터
	private String getPostData(HttpServletRequest request) throws IOException {
		// 문자열을 계속 이어붙이는 데 사용
		// 문자열데이타를 저장하기위한 기억장소생성
		StringBuilder postData = new StringBuilder();
		
		// 클라이언트가 요청한 정보(주소)를 읽어와서 reader라는 임시기억장소에 저장.
		BufferedReader reader = request.getReader();
		String line;// 한 줄의 데이터를 담을 변수
		
		// reader객체가 가지고 있는 정보를 한줄씩 읽어와서 postData객체가 가리키는 기익장소에 추가시킨다.
		while((line = reader.readLine()) != null) {
			postData.append(line);//line에 저장된 데이터를 postData에 추가 ex) postData.append("username=abc")
		}
		return postData.toString();// "username=abc&password=123"
		
	}

	// 인증되지 않은 상태에서 원래요청한 주소(URI)의 정보를 저장하는 기능
	private void getTargetUrl(HttpServletRequest request) {
		
		// http://localhost:8888/userinfo/modify?userid=user01 주소요청
		String uri = request.getRequestURI(); // /userinfo/modify
		String query = request.getQueryString();// ?물음표 뒤의 문자열   ?userid=user01
		
		if(query == null || query.equals("null")) {// 쿼리스트링이 없을 경우
			query = "";
		}else { // 쿼리스트링이 있을 경우
			query = "?" + query;// ?userid=user01
		}
		
		String targetUrl = uri + query; // /userinfo/modify?userid=user01
		
		// 클라이언트가 요청한 방식이 get방식일 경우.
		if(request.getMethod().equals("GET")) {
			// 인증되지 않은 사용자가 원래요청한 주소및 쿼리스트링을 세션에 저장하고, 로그인에서 참조하고 자 할 경우 목적
			request.getSession().setAttribute("targetUrl", targetUrl);
		}
	}

	/*
	사용자가 로그인하지 않았을 경우:
	AJAX 요청이라면, 데이터를 세션에 저장하고 HTTP 400 오류를 응답
	일반 요청이라면 로그인 페이지로 리다이렉트

	사용자가 로그인한 경우:
	요청을 계속 처리하도록 true를 반환하여 정상적으로 실행
	 */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
