package com.cosmetic.shop.review;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class ReviewService {

	private final ReviewMapper reviewMapper;
}
