package com.cosmetic.shop.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//인터셉터기능을 갖는 클래스는 HandlerInterceptor 인터페이스를 상속받아 구현해야 한다.
@Component
public class LoginInterceptor implements HandlerInterceptor {

	// 인터셉터 클래스가 관리하는 URI 요청이 발생되면, 인터셉터가 가로채서 preHandle()메서드가 먼저 동작(실행)
	// HttpServletRequest request : 클라이언트가 요청한 정보를 가지고 있는 객체.
	// HttpServletResponse response : 서버에서 클라이언트로 보내는 응답정보를 가지고 있는 객체.
		
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// HttpServletRequest request : 클라이언트가 요청한(보내온) 모든 정보(데이타)를 서버에서 관리하는 객체.
		// HttpServletResponse response : 서버에서 클라이언트로 보낼 응답정보를 관리하는 객체.
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	

	
	
	
	
	
	
}
