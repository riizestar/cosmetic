package com.cosmetic.shop.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

	// OrderVO안에는 ord_code가 null인 상태이지만, mapper파일에서 insert문이 작동되면 auto_increment의 기능에 의하여 일련번호가
	// ord_code 컬럼에 적용된다. 그리고 그 값을 keyProperty속성 설정에 ord_code필드가 일련번호를 참조하게 된다.
	
	void order_insert(OrderVO vo);
	
	void order_detail_insert(@Param("ord_code") Integer ord_code,@Param("m_id") String m_id);
	
	List<Map<String, Object>> getOrdInfoByOrd_code(Integer ord_code);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
