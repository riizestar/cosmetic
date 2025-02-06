package com.cosmetic.shop.review;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cosmetic.shop.product.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController 
@RequiredArgsConstructor
@RequestMapping("/review/*") // /review로 시작하는 모든주소는 ReviewController 클래스가 처리한다.
public class ReviewController {
	
	private final ReviewMapper reviewMapper;
	private final ProductService productService;

}
