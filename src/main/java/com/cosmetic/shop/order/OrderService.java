package com.cosmetic.shop.order;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
	
	public final OrderMapper orderMapper;

}
