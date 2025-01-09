package jp.co.sss.shop.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 * 商品一覧画面 Controller
 * 
 */
@Controller
@RequiredArgsConstructor
public class ProductListController extends CommonController {
	
	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;
	
	@GetMapping("/productList")
	public String productList(HttpSession session, Model model, Integer productId) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		model.addAttribute("products", productService.findAllProducts());
		return "product/product_list";
	}
	
	@PostMapping("/findProductList")
	public String findProductList(@RequestParam("productName")String productName,
			@RequestParam(value = "categoryList", required = false) Integer categoryList,
			HttpSession session, Model model) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		findProducts(productName, categoryList, model, productService); // 検索結果を追加
		return "product/product_list";
	}
}
