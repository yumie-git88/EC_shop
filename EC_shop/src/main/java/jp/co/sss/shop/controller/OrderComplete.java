package jp.co.sss.shop.controller;

import java.math.BigDecimal;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.form.PurchaseDetailForm;
import jp.co.sss.shop.service.OrderService;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/** 完了画面 **/
@Controller
@RequiredArgsConstructor
public abstract class OrderComplete extends CommonController {
	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;
	private final OrderService orderService;

	@GetMapping("/OrderComplete")
	public String cartList(HttpSession session, Model model, Integer productId) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			return "redirect:/login";
		}
		
		// Orderテーブルから合計金額を取得
		Integer orderId = (Integer) session.getAttribute("orderId");
		if (orderId == null) {
			model.addAttribute("errorMessage", "注文IDが見つかりませんでした。");
		}
		
		Order order = orderService.findOrderById(orderId);
		if (order == null) {
			model.addAttribute("errorMessage", "注文情報が見つかりませんでした。");
		}
		BigDecimal totalAmount = order.getTotalAmount();
		model.addAttribute("totalAmount", totalAmount);
		
		PurchaseDetailForm form = (PurchaseDetailForm) session.getAttribute("purchaseDetailForm");
		if (form == null) {
			model.addAttribute("errorMessage", "購入詳細フォームが見つかりませんでした。");
			return "redirect:/";
		}
		model.addAttribute("purchaseDetailForm", form);
		return "cart/order_complete";
	}
}
