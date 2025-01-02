package com.cosmetic.shop.member;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

//표준패키지구조에서는 인터페이스로 생성했으나, 현 구조는 클래스를 생성함
@RequiredArgsConstructor
@Service
public class MemberService {
	
	private final MemberMapper memberMapper;// 매퍼인터페이스를 참조하는객체를 사용하기 위해서
	
	public String idCheck(String m_id) {
		return memberMapper.idCheck(m_id);
	}

	public void join(MemberVO vo) {
		memberMapper.join(vo);
	}
	
	public MemberVO login(String m_id) {
		return memberMapper.login(m_id);
	}
	
	public void pwchange(String m_id, String m_password) {
		memberMapper.pwchange(m_id, m_password);
	}
	
	
	// 홍길동  abc@abc.com
	public String idsearch(String m_name, String m_email) {//저장
		return memberMapper.idsearch(m_name, m_email);//읽어옴
	}
	
	public String pwtemp_confirm(String m_id, String m_email) {
		return memberMapper.pwtemp_confirm(m_id, m_email);
	}
	
	public MemberVO modify(String m_id) {
		return memberMapper.modify(m_id);
	}
	
	public void modify_save(MemberVO vo) {
		memberMapper.modify_save(vo);
	}
	
	
}