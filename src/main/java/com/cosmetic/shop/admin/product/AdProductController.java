package com.cosmetic.shop.admin.product;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.cosmetic.shop.admin.category.AdCategoryService;
import com.cosmetic.shop.admin.category.CategoryVO;
import com.cosmetic.shop.common.constants.Constants;
import com.cosmetic.shop.common.utils.FileUtils;
import com.cosmetic.shop.common.utils.PageMaker;
import com.cosmetic.shop.common.utils.SearchCriteria;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//관리자 : 상품관리기능
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product/*")
public class AdProductController {

	private final AdProductService adProductService;
	private final AdCategoryService adCategoryService;
	
	// application.properties 의존성 주입
	@Value("${com.ezen.upload.path}")
	private String uploadPath;
	
	@Value("${com.ezen.upload.ckeditor.path}")
	private String uploadCKPath;
	
	// 상품이미지 관련작업기능
	private final FileUtils fileUtils;

	// 상품등록 폼.  1차카테고리 정보를 출력.	
	@GetMapping("/pro_insert")
	public void pro_insert(Model model) {
		
		// 1차카테고리 목록
		model.addAttribute("cate_list", adCategoryService.getFirstCategoryList());
	}
	
	// 상품등록시 CKEditor에서 사용하는 상품설명이미지 업로드작업
	// ckeditor 에서 사용하는 업로드. 클라이언트에서 보낸 파라미터명과 스프링에서 받는 파라미터명이 동일해야 한다.(규칙)
	@PostMapping("/imageupload")
	public void imageupload(HttpServletRequest request, HttpServletResponse response, MultipartFile upload) throws Exception {
		
	
		// 데이타를 바이트단위로 작업하는 출력스트림의 최상위클래스(추상)
		OutputStream out = null;
		PrintWriter printWriter = null; // 서버에서 클라이언트에게 응답정보를 보낼때 사용(업로드 파일정보)
		
		// 예외처리문법
		try {
			//1)CKeditor를 이용한 파일업로드 작업.
			String fileName = upload.getOriginalFilename(); // 클라이언트에서 업로드 파일명.   예>abc.gif
			byte[] bytes = upload.getBytes(); // 업로드하는 파일(abc.gif)을 나타내는 바이트배열
			
			// C:\\Dev\\upload\\ckeditor\\abc.gif
			String ckUploadPath = uploadCKPath + "\\" + fileName;
			
			//스트림 out객체생성이 되면, 해당 경로에 파일은 생성된다. 파일크기는 0byte
			out = new FileOutputStream(ckUploadPath);
			
			out.write(bytes); // out스트림객체에 파일 byte배열을 채웠다.
			out.flush(); //out스트림객체에 존재하고 있는 byte배열을 빈파일에 쓰는 작업.
			
			//2)업로드한 파일정보를 클라이언트인 CKEditor로 보내주는 작업.
			// printWriter : 파일정보를 클라이언트쪽에 보낼때 사용하는 객체.
			printWriter = response.getWriter();
			
			// 매핑주소
			String fileUrl = "/admin/product/display/" + fileName;
			
			// ckeditor.js 4.12에서 파일정보를 아래와 같이 작업을 하도록 가이드
			// 파일정보를 JSON 데이타표현 형식 {"filename" : "abc.gif","uploaded":1,"url":"/display/abc.gif"} 
			printWriter.println("{\"filename\" :\"" + fileName + "\", \"uploaded\":1,\"url\":\"" + fileUrl + "\"}"); // 스트림에 채움.
				
			printWriter.flush();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			
			// 객체소멸은 객체생성의 역순으로 close()작업해준다.(이론)
			// out, printWriter 객체는 순서의 의미는 없다.
			if(out != null) {
				try {
					out.close(); // 메모리 소멸
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			if(printWriter != null) printWriter.close(); // 메모리 소멸
		}
	}
	
	//CKEditor에서 업로드된후 보여주는 기능
	// 이미지파일을 CKEditor를 통하여 화면에 출력하기
	@GetMapping("/display/{fileName}")
	public ResponseEntity<byte[]> getFile(@PathVariable("fileName") String fileName) {
		ResponseEntity<byte[]> entity = null;
		
		try {
			entity = fileUtils.getFile(uploadCKPath, fileName);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		return entity;
		
	}
	
	// 상품등록(저장).  <input type="file" name="pro_img_upload">
	@PostMapping("/pro_insert")
	public String pro_insert(ProductVO vo, MultipartFile pro_img_upload) throws Exception {
		
		//1)상품이미지 파일업로드작업
		String dateFolder = fileUtils.getDateFolder(); // 상품이미지 업로드되는 날짜폴더이름
		// 상품이미지 업로드및 실제사용한 업로드 파일명
		String saveFileName = fileUtils.uploadFile(uploadPath, dateFolder, pro_img_upload);
		
		// 업로드한 날짜과 저장.   
		vo.setPro_up_folder(dateFolder);
		// 상품이미지명을 실제 업로드한 파일이름으로 저장
		vo.setPro_img(saveFileName);
		//2)상품정보 DB저장
		adProductService.pro_insert(vo);
		
		return "redirect:/admin/product/pro_list";
	}
	
		// 상품목록 - 테이블의 데이타를 출력(select문)
		// 스프링부트 컨트롤러의 매핑주소로부터 호출되는 메서드의 파라미터가 참조타입이면,
		// 스프링부트 시스템이 내부적으로 객체생성을 자동으로 생성해준다.
		// cri객체가 가리키는 기억장소에 page=1, perPageNum=10, searchType=null, keyword=null 4개의 필드가 존재한다.
		@GetMapping("/pro_list") // SearchCriteria cri = new SearchCriteria()
		public void pro_list(SearchCriteria cri, Model model) throws Exception {
			
		cri.setPerPageNum(Constants.ADMIN_PRODUCT_LIST_COUNT); // 페이지별 2건
		
		//1)상품목록
		List<ProductVO> pro_list = adProductService.pro_list(cri);
		
		// 날짜폴더의 \를 /로 변환하는 작업
		pro_list.forEach(vo -> {
			vo.setPro_up_folder(vo.getPro_up_folder().replace("\\", "/"));	
		});
		
		model.addAttribute("pro_list", pro_list); // 타임리프 페이지서 사용이 가능
		
		//2)페이징정보
		PageMaker pageMaker = new PageMaker();
		
		pageMaker.setDisplayPageNum(Constants.ADMIN_PRODUCT_LIST_PAGESIZE);
		
		pageMaker.setCri(cri); // cri 기억장소에 page=1, perPageNum=10, searchType=null, keyword=null 4개의 필드
		pageMaker.setTotalCount(adProductService.getTotalCount(cri));
		
		log.info("페이지정보" + pageMaker);
		
		model.addAttribute("pageMaker", pageMaker);
		
		// 1차카테고리 목록
		model.addAttribute("cate_list", adCategoryService.getFirstCategoryList());
		
		
		}
		
		// 상품목록 이미지출력하기.. 클라이언트에서 보낸 파라미터명 스프링의 컨트롤러에서 받는 파라미터명이 일치해야 한다.
		@GetMapping("/image_display")
		public ResponseEntity<byte[]> image_display(String dateFolderName, String fileName) throws Exception {
			
			return fileUtils.getFile(uploadPath + "\\" + dateFolderName, fileName);
		}
		
		// 상품수정 폼
		// @ModelAttribute("cri") : 파라미터의 값을 타임리프페이지에서 사용하기위한 목적
		@GetMapping("/pro_edit")
		public void pro_edit(@ModelAttribute("cri") SearchCriteria cri, Integer pro_num, Model model) throws Exception {
			
			log.info("페이징및검색정보: " + cri);
			log.info("상품코드: " + pro_num);
			
			// 1차카테고리 목록
			// 1)1차카테고리 목록
			model.addAttribute("cate_list", adCategoryService.getFirstCategoryList());
			
			// 2)상품정보.(2차카테고리 코드)
			ProductVO productVO = adProductService.pro_edit_form(pro_num);
			// 날짜폴더에 역슬래시가 클라이언트에서 서버로 보내질때 에러가 발생된다.
			// 그래서, 미리 서버에서 클라이언트로 보낼때 역슬래시를 슬래시로 변환하여, 클라이언트에서 문제를 미연에 방지한다.
			productVO.setPro_up_folder(productVO.getPro_up_folder().replace("\\", "/"));
			model.addAttribute("productVO", productVO);
			
			// 상품정보의 있는 2차카테고리 코드.
			int secondCategory = productVO.getCate_code();
			
			// 3) 2차카테고리의 부모인 1차카테고리 정보.(실제 상품에 해당하는 1차카테고리 정보)
			CategoryVO categoryVO = adCategoryService.getFirstCategoryBySecondCategory(secondCategory);
			model.addAttribute("categoryVO", categoryVO);
			
			/*** 1차카테고리 목록출력하고, 현재 상품의 1차카테고리코드 선택상태 */
			
			// 1차카테고리 코드
			int firstCategory = categoryVO.getCate_prtcode();
			
			// 1차카테고리 코드를 부모로하는 2차카테고리 목록.
			model.addAttribute("secondCategoryVO", adCategoryService.getSecondCategoryList(firstCategory));
					
		}
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
