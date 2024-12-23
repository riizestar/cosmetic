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
	void pwchange(@Param("m_id") String m_id, @Param("m_password") String m_password);// @Param은 파라미터타입 = "Map"
	
	// 아이디찾기   
	String idsearch(@Param("m_name") String m_name, @Param("m_email") String m_email);

	// 임시비밀번호 발급하기위한 아이디와메일주소 존재여부체크
	String pwtemp_confirm(@Param("m_id") String m_id, @Param("m_email") String m_email);
	
	// 회원수정 폼
	MemberVO modify(String m_id);
	
	// 회원수정
	void modify_save(MemberVO vo);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
