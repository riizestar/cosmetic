package com.cosmetic.shop.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cosmetic.shop.payment.PaymentVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
	
	public final OrderMapper orderMapper;
	
	// 주문하기.(주문테이블, 주문상세테이블(주문번호), 결제테이블(주문번호), 배송테이블(주문번호),장바구니테이블)
	@Transactional
	public void order_process(OrderVO vo, String m_id,String p_method) {
		
		log.info("주문번호: " + vo.getOrd_code());
		
		// 1)주문테이블 : 주문자정보,배송지정보를 브라우저 폼에서 입력받아서 데이타를 사용
		orderMapper.order_insert(vo);
		
		log.info("주문번호: " + vo.getOrd_code()); // 주문번호 출력
		
		// 2)주문상세테이블(데이타베이스의 장바구니테이블의 로그인한 사용자가 저장시킨 데이트를 이용하여, 주문상세테이블에 주문상품정보를 저장시킨다.)
		// 장바구니에서 주문상세로 변환 저장 작업. 
		// 이 작업을 통해 주문 후 상품 정보를 확인하거나, 배송, 결제 등과 관련된 후속 처리를 할 수 있게 됨.
		orderMapper.order_detail_insert(vo.getOrd_code(),m_id);
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
