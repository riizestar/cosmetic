package com.cosmetic.shop.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cosmetic.shop.cart.CartMapper;
import com.cosmetic.shop.common.utils.SearchCriteria;
import com.cosmetic.shop.delivery.DeliveryMapper;
import com.cosmetic.shop.delivery.DeliveryVO;
import com.cosmetic.shop.payment.PaymentMapper;
import com.cosmetic.shop.payment.PaymentVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
	
	public final OrderMapper orderMapper;
	public final PaymentMapper paymentMapper;
	public final CartMapper cartMapper;
	public final DeliveryMapper deliveryMapper;
	
	// 주문하기.(주문테이블, 주문상세테이블(주문번호), 결제테이블(주문번호), 배송테이블(주문번호),장바구니테이블)
	@Transactional
	public void order_process(OrderVO vo, String m_id,String p_method) {
		
		log.info("주문번호: " + vo.getOrd_code());
		
		// 1)주문테이블 : 주문자정보,배송지정보를 브라우저 폼에서 입력받아서 데이타를 사용
		orderMapper.order_insert(vo);
		
		log.info("주문번호: " + vo.getOrd_code()); // 주문번호 출력
		
		// 2)주문상세테이블(데이타베이스의 장바구니테이블의 로그인한 사용자가 저장시킨 데이트를 이용하여, 주문상세테이블에 주문상품정보를 저장시킨다.)
		// 장바구니에서 주문상세로 변환 저장 작업. (장바구니테이블의 정보를 이용하여 작업)
		// 이 작업을 통해 주문 후 상품 정보를 확인하거나, 배송, 결제 등과 관련된 후속 처리를 할 수 있게 됨.
		orderMapper.order_detail_insert(vo.getOrd_code(),m_id);
		
		// 3)결제테이블
		PaymentVO p_vo = new PaymentVO();
		p_vo.setOrd_code(vo.getOrd_code());
		p_vo.setM_id(m_id);
		
		p_vo.setPayment_method(p_method); // "계좌이체"
		p_vo.setPayment_price(vo.getOrd_price());// 총 구매금액
		
		if(p_method.equals("카카오페이")) {
			p_vo.setPayment_status("입금완료");
		}else if(p_method.contains("계좌이체")) {
			p_vo.setPayment_status("입금미납");
		}
		
		paymentMapper.payment_insert(p_vo);
		
		// 4)장바구니 테이블
		cartMapper.cart_empty(m_id);// 결제하고 나면 장바구니 테이블 비움
		
		// 5)배송테이블
		DeliveryVO deliveryVO = new DeliveryVO();
		deliveryVO.setOrd_code(vo.getOrd_code()); // 주문번호
		deliveryVO.setShipping_zipcode(vo.getOrd_addr_zipcode());
		deliveryVO.setShipping_addr(vo.getOrd_addr_basic());
		deliveryVO.setShipping_deaddr(vo.getOrd_addr_detail());
		
		deliveryMapper.delivery_insert(deliveryVO);
		
	}
	
	// 실시간 결제에 따른 주문상세내역에 주문내역
	public List<Map<String, Object>> getOrdInfoByOrd_code(Integer ord_code){
		return orderMapper.getOrdInfoByOrd_code(ord_code);
	}
	
	public List<Map<String, Object>> getOrderInfoByUser_id(String m_id, SearchCriteria cri){
		return orderMapper.getOrderInfoByUser_id(m_id, cri);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
