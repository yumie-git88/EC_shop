package jp.co.sss.shop.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.sss.shop.repository.SaleItemRepository;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 * TOP画面 Controller
 * 
 */
@Controller
@RequiredArgsConstructor
public class indexController extends CommonController {
	
	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;
	
	/** TOP情報テーブルDAO */
	private final SaleItemRepository repository;
	
	/**
	 * 初期表示
	 * @param model モデル
	 * @param session セッションにユーザーネーム保存
	 * @param productId 商品ID
	 * @return 表示画面
	 */
	@GetMapping("/")
	public String index(HttpSession session, Model model, Integer productId) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		model.addAttribute("saleItems", repository.findAll());
		return "index";
	}
}
