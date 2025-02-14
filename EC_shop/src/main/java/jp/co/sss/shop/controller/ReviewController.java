package jp.co.sss.shop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.entity.Product;
import jp.co.sss.shop.entity.Review;
import jp.co.sss.shop.form.ReviewRegisterForm;
import jp.co.sss.shop.repository.ReviewRepository;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 * レビュー新規登録画面 Controller
 */
@Controller
@RequiredArgsConstructor
public class ReviewController extends CommonController{
	
	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@GetMapping("/reviewRegister")
	public String reviewRegister(HttpSession session, Model model,
			@RequestParam(value = "productId", required = false) Integer productId) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		Object userIdObj = session.getAttribute("userId"); // 変換
		Integer userId = userIdObj != null ? Integer.parseInt(userIdObj.toString()) : null;
		model.addAttribute("userId", userId);
		
		Product product = productService.getProductById(productId);
		if (product == null) {
			model.addAttribute("errorMessage", "商品が見つかりません");
			return "error";
		}
		model.addAttribute("product", product);
		return "review/reviewRegister";
	}

	@PostMapping("/reviewRegisterPost")
	public String registerReview(@ModelAttribute @Valid ReviewRegisterForm form, HttpSession session, Model model) {
		System.out.println("Comment: " + form.getComment());
		System.out.println("Email: " + form.getEmail());
		System.out.println("ProductId: " + form.getProductId());
		
		Object userIdObj = session.getAttribute("userId");
		Integer userId = userIdObj != null ? Integer.parseInt(userIdObj.toString()) : null;
		
		Review review = new Review();
		review.setUserId(userId);
		review.setProductId(form.getProductId());
		review.setRating(form.getRating());
		review.setComment(form.getComment());
		review.setDummyUserName(form.getDummyUserName());
		review.setReviewImgPath(form.getReviewImgPath());
		
		reviewRepository.save(review);
		model.addAttribute("message", "口コミが正常に登録されました！");
		return "product/product_list";
	}
}
