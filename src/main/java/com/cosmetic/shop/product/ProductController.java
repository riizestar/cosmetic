package com.cosmetic.shop.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@Controller
public class ProductController {

	private final ProductService productService;
	
	@Value("${com.ezen.upload.path}")
	private String uploadPath;
	
	@Value("${com.ezen.upload.ckeditor.path}")
	private String uploadCKPath;
}
