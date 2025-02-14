package jp.co.sss.shop.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.shop.entity.Cart;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.Product;
import jp.co.sss.shop.form.PurchaseDetailForm;
import jp.co.sss.shop.service.OrderService;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 * 購入確認画面 Controller 情報画面
 */
@Controller
@RequiredArgsConstructor
public class PurchaseConfirmationController extends CommonController {

	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;

	@Autowired
	private OrderService orderService;

	/** カートサービス */
	//	private final CartService cartService;

	/** 購入確認画面 
	 * @param session セッション
	 * @param model モデル
	 * @param productId 商品ID
	 * @return 表示
	 * **/
	@GetMapping("/purchaseConfirmation")
	public String purchaseConfirmation(HttpSession session, Model model, Integer productId) {
		addCommonAttributes(session, model, productService);

		List<Cart> cartList = new ArrayList<>();
		BigDecimal totalPriceIncludingTax = BigDecimal.ZERO;
		int totalQuantity = 0;

		if (productId != null) {
			// 単品購入の場合
			Product product = productService.getProductById(productId);

			if (product == null) {
				model.addAttribute("errorMessage", "商品が見つかりません");
				return "error";
			}

			// 商品の数量を1に設定
			Cart singleCartItem = new Cart();
			singleCartItem.setProduct(product);
			singleCartItem.setQuantity(1);

			cartList.add(singleCartItem);
			totalPriceIncludingTax = product.getTaxPrice();
			totalQuantity = 1;

			// モデルに数量と合計金額を設定
			model.addAttribute("totalQuantity", totalQuantity);
			model.addAttribute("totalPriceIncludingTax", totalPriceIncludingTax);
		} else {
			// カートにある商品を表示
			Object cartListObj = session.getAttribute("cartList");
			if (cartListObj instanceof List<?>) {
				cartList = ((List<?>) cartListObj).stream()
						.filter(item -> item instanceof Cart)
						.map(item -> (Cart) item)
						.collect(Collectors.toList());
			}

			// 商品をグループ化して数量を合計
			Map<Integer, Cart> groupedCartMap = new HashMap<>();
			for (Cart cartItem : cartList) {
				int cartProductId = cartItem.getProduct().getProductId();
				if (groupedCartMap.containsKey(cartProductId)) {
					Cart existingItem = groupedCartMap.get(cartProductId);
					existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
				} else {
					groupedCartMap.put(cartProductId, cartItem);
				}
			}
			cartList = new ArrayList<>(groupedCartMap.values());

			// カート内の全個数を計算
			totalQuantity = cartList.stream().mapToInt(Cart::getQuantity).sum();

			// カート内の全商品の合計金額（税込み）
			totalPriceIncludingTax = cartList.stream()
					.map(cartItem -> cartItem.getProduct().getTaxPrice()
							.multiply(BigDecimal.valueOf(cartItem.getQuantity())))
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			// モデルに数量と合計金額を設定
			model.addAttribute("totalQuantity", totalQuantity);
			model.addAttribute("totalPriceIncludingTax", totalPriceIncludingTax);
		}

		model.addAttribute("cartList", cartList);

		// カート内の各商品の商品名を取得してモデルに追加
		List<String> productNames = cartList.stream()
				.map(cartItem -> cartItem.getProduct().getProductName())
				.collect(Collectors.toList());
		List<Integer> productStocks = cartList.stream()
				.map(cartItem -> cartItem.getProduct().getStock())
				.collect(Collectors.toList());
		BigDecimal totalAmount = cartList.stream()
				.map(Cart::getTotalAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		model.addAttribute("productName", productNames);
		model.addAttribute("quantity", totalQuantity); // 数量を設定
		model.addAttribute("productStock", productStocks);
		model.addAttribute("totalAmount", totalAmount);

		return "purchase/purchaseConfirmation";
	}

	/** 購入確認画面から完了画面へ 購入内容の詳細
	 * @param session セッション
	 * @param model モデル
	 * @param productId 商品
	 * @return 購入確認画面 表示
	 *  **/
	@PostMapping("/orderComplete")
	public String orderComplete(
			HttpSession session, Model model, Integer productId) {
		addCommonAttributes(session, model, productService);
		Object cartListObj = session.getAttribute("cartList"); // カートにある商品を表示
		List<Cart> cartList;
		if (cartListObj instanceof List<?>) {
			cartList = ((List<?>) cartListObj).stream()
					.filter(item -> item instanceof Cart)
					.map(item -> (Cart) item)
					.collect(Collectors.toList());
		} else {
			cartList = new ArrayList<>();
		}

		// カート内の各商品の商品名を取得してモデルに追加
		List<String> productNames = cartList.stream()
				.map(cartItem -> cartItem.getProduct().getProductName())
				.collect(Collectors.toList());
		List<Integer> quantities = cartList.stream()
				.map(cartItem -> cartItem.getQuantity())
				.collect(Collectors.toList());
		BigDecimal totalAmount = cartList.stream()
				.map(cartItem -> cartItem.getProduct().getTaxPrice()
						.multiply(BigDecimal.valueOf(cartItem.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		model.addAttribute("productName", productNames);
		model.addAttribute("quantity", quantities);
		model.addAttribute("totalAmount", totalAmount);

		PurchaseDetailForm form = (PurchaseDetailForm) session.getAttribute("purchaseDetailForm");
		if (form == null) {
			model.addAttribute("errorMessage", "購入詳細フォームが見つかりませんでした。");
			return "error";
		}

		Order order = new Order();
		String userIdStr = (String) session.getAttribute("userId");
		if (userIdStr != null) {
			order.setUserId(Integer.parseInt(userIdStr));
		}
		order.setTotalAmount(totalAmount);
		order.setStatus("Pending");
		order = orderService.saveOrder(order);

		final Order finalOrder = order; // Orderを最終的な変数に設定

		// OrderItemsを作成して保存
		List<OrderItem> orderItems = cartList.stream().map(cartItem -> {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(finalOrder);
			orderItem.setOrderId(finalOrder.getOrderId());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setProductId(cartItem.getProduct().getProductId());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setPrice(cartItem.getProduct().getTaxPrice());
			return orderItem;
		}).collect(Collectors.toList());
		orderService.saveOrderItems(orderItems);

		// 注文完了後にカートをクリア
		session.removeAttribute("cartList");

		// 注文完了画面に購入情報を渡す
		model.addAttribute("purchaseDetailForm", form);
		return "cart/order_complete";
	}
}
