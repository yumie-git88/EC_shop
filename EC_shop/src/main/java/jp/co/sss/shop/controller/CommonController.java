package jp.co.sss.shop.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Product;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 * ヘッダー Contoroller
 */
@Controller
@RequiredArgsConstructor
public class CommonController {

	/**
	 * ユーザー名・カテゴリー取得
	 * 
	 * @param session モデル
	 * @param model セッションにユーザーネーム保存
	 * @param productService 商品カテゴリー一覧
	 */
	protected void addCommonAttributes(HttpSession session, Model model, ProductService productService) {
		Object userName = session.getAttribute("userName");
		model.addAttribute("userName", (String) userName);
		List<Category> categories = productService.findAllCategories();
		model.addAttribute("categories", categories);
//		model.addAttribute("products", productService.findAllProducts());
	}
	
	/**
	 * 商品名・カテゴリー検索
	 * 
	 * @param productName 商品名
	 * @param categoryId カテゴリーID
	 * @param model モデル
	 * @param productService 商品検索
	 */
	protected void findProducts(String productName, Integer categoryId, Model model, ProductService productService) {
		List<Product> products = productService.findProductNameAndCategory(productName, categoryId);
		model.addAttribute("products", products);
		if (products.isEmpty()) {
			model.addAttribute("message", "該当する商品が見つかりませんでした。");
		}
	}
}
