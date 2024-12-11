package com.cosmetic.shop.member;

import org.apache.ibatis.annotations.Param;

public interface MemberMapper {
	
	// 아이디 체크
	String idCheck(String m_id);
	
	// 회원정보저장
	void join(MemberVO vo);
	
	// 로그인 처리
	MemberVO login(String m_id);
	
	// mapper인터페이스에서 파라미터가 2개이상이면, @Param 어노테이션을 사용해야 한다.(규칙)
	// 비밀번호 변경
	void pwchange(@Param("m_id") String m_id, @Param("m_password") String m_password);
	
	

}
