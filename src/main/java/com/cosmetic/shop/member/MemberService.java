package com.cosmetic.shop.member;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

//표준패키지구조에서는 인터페이스로 생성했으나, 현 구조는 클래스를 생성함
@RequiredArgsConstructor
@Service
public class MemberService {
	
	private final MemberMapper memberMapper;// 
	
	public String idCheck(String m_id) {
		return memberMapper.idCheck(m_id);
	}

	public void join(MemberVO vo) {
		memberMapper.join(vo);
	}
	
	public MemberVO login(String m_id) {
		return memberMapper.login(m_id);
	}
	
	
}
