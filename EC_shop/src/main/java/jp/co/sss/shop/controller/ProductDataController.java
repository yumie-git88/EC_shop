package jp.co.sss.shop.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jp.co.sss.shop.repository.ProductListRepository;
import jp.co.sss.shop.repository.ReviewRepository;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 * 商品詳細画面 Controller
 * 
 */
@Controller
@RequiredArgsConstructor
//@RequestMapping("/shop")
public class ProductDataController extends CommonController {
	
	/**	商品情報テーブルDAO */
	private final ProductListRepository repository;
	
	/**	レビュー情報テーブルDAO */
	private final ReviewRepository reviewRepository;
	
	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;
	
	/**
	 * 商品詳細表示
	 * @param session 共通の属性を追加
	 * @param id 商品ID取得
	 * @param model モデル
	 * @param productId 商品ID
	 * @return 商品詳細画面
	 */
	@GetMapping("/product/{id}")
	public String productData(HttpSession session, @PathVariable("id") Integer id, Model model, Integer productId) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		model.addAttribute("products", productService.findAllProducts());
		var product = repository.findById(id);
		if (product.isPresent()) {
			model.addAttribute("product", product.get());
			var reviews = reviewRepository.findByProductId(id);
			model.addAttribute("reviews", reviews);
			return "product/productDetail";
		} else {
//			return "error/404"; // 商品が見つからない場合のエラーページ
			return "redirect:/"; // 商品が見つからない場合のエラーページ
		}
	}
}
