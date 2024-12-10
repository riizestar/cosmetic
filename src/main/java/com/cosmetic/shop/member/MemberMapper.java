package com.cosmetic.shop.member;

import org.apache.ibatis.annotations.Param;

public interface MemberMapper {
	
		// 아이디 체크
		String idCheck(String m_id);
		
		// 회원정보저장
		void join(MemberVO vo);
		
		// 로그인 처리
		MemberVO login(String m_id);
		
		

}
