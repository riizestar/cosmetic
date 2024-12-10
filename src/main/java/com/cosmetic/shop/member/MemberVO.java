package com.cosmetic.shop.member;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberVO {

	private String m_id; 
	private String m_name; 
	private String m_password; 
	private String m_zipcode; 
	private String m_addr; 
	private String m_deaddr; 
	private String m_phone; 
	private String m_email; 
	private String m_receive;
	private int    m_point;
	private Date   m_datesub;
	private Date   m_updatedate;
	private Date   m_lastlogin;
	 
}
