package jp.co.sss.shop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.entity.Cart;
import jp.co.sss.shop.entity.Product;
import jp.co.sss.shop.service.OrderService;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 * カート追加画面 Controller
 */
@Controller
@RequiredArgsConstructor
public class CartController extends CommonController {

	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;
	private final OrderService orderService;

	/**
	 * カート追加画面表示
	 * @param session セッション
	 * @param model モデル
	 * @param productId 商品ID
	 * @return 表示
	 */
	@GetMapping("/cartAdd")
	public String cartAdd(HttpSession session, Model model,
			@RequestParam(value = "productId", required = false) Integer productId) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		Object userIdObj = session.getAttribute("userId"); // 変換
		Integer userId = userIdObj != null ? Integer.parseInt(userIdObj.toString()) : null;
		model.addAttribute("userId", userId);

		Object cartListObj = session.getAttribute("cartList");
		List<Cart> cartList;
		if (cartListObj instanceof List<?>) {
			cartList = ((List<?>) cartListObj).stream()
					.filter(item -> item instanceof Cart)
					.map(item -> (Cart) item)
					.collect(Collectors.toList());
		} else {
			cartList = new ArrayList<>();
		}
		if (productId != null) {
			Product product = productService.getProductById(productId);
			if (product != null) {
				Cart cartItem = new Cart();
				cartItem.setProduct(product);
				cartItem.setQuantity(1); // 個数を1に設定
				cartList.add(cartItem);
				session.setAttribute("cartList", cartList);
				model.addAttribute("product", product);
				model.addAttribute("cartItem", cartItem);
			} else {
				model.addAttribute("cartMessage", "商品が見つかりませんでした");
			}
		} else {
			model.addAttribute("cartMessage", "カートに商品がありません");
		}
		model.addAttribute("cartList", cartList);
		return "cart/cart_add";

	}

	/**
	 * カートに移動ボタン カート詳細へ
	 * @param session セッション
	 * @param model モデル
	 * @param userIdStr ユーザーID
	 * @param quantity 数量
	 * @return 表示
	 */
	@PostMapping("/cartList")
	public String createOrder(HttpSession session, Model model, @RequestParam("userId") String userIdStr,
			@RequestParam("quantity") Integer quantity) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		Integer userId = Integer.parseInt(userIdStr);
		orderService.createOrder(session, userId);
		Object cartListObj = session.getAttribute("cartList");
		List<Cart> cartList;
		if (cartListObj instanceof List<?>) {
			cartList = ((List<?>) cartListObj).stream()
					.filter(item -> item instanceof Cart)
					.map(item -> (Cart) item)
					.collect(Collectors.toList());
		} else {
			cartList = new ArrayList<>();
		}
		model.addAttribute("cartList", cartList);
		model.addAttribute("cartMessage", "カートに商品が追加されました");
		return "cart/cart_list";
	}

	/**
	 * カート追加画面 レジに進むボタン→購入品詳細画面
	 * @param session セッション保存
	 * @param model モデル
	 * @param productId 商品TD
	 * @return 表示
	 */
	@PostMapping("/CartPurchaseDetail")
	public String purchaseDetail(HttpSession session, Model model,
			@RequestParam(value = "productId", required = true) Integer productId) {
		addCommonAttributes(session, model, productService);

		Object cartListObj = session.getAttribute("cartList");
		List<Cart> cartList;
		if (cartListObj instanceof List<?>) {
			cartList = ((List<?>) cartListObj).stream()
					.filter(item -> item instanceof Cart)
					.map(item -> (Cart) item)
					.collect(Collectors.toList());
		} else {
			cartList = new ArrayList<>();
		}

		Product product = productService.getProductById(productId);
		if (product != null) {
			Cart cartItem = cartList.stream()
					.filter(item -> item.getProduct().getProductId().equals(productId))
					.findFirst().orElse(null);
			if (cartItem != null) {
				model.addAttribute("product", product);
				model.addAttribute("cartItem", cartItem);
			} else {
				model.addAttribute("cartMessage", "カートに商品が見つかりませんでした");
			}
		} else {
			model.addAttribute("cartMessage", "商品が見つかりませんでした");
		}

		model.addAttribute("cartList", cartList);
		return "purchase/purchaseDetail";
	}
}
