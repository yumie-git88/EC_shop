package jp.co.sss.shop.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.entity.Cart;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 * カート詳細画面 Controller
 */
@Controller
@RequiredArgsConstructor
public class CartListController extends CommonController {

	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;

//	private final CartService cartService;

	/**
	 * カート詳細画面表示 カート内容
	 * @param session セッション
	 * @param model モデル
	 * @param productId 商品ID
	 * @param quantity 数量
	 * @return 表示
	 */
	@GetMapping("/cartList")
	public String cartList(HttpSession session, Model model, Integer productId,
			Integer quantity) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			return "redirect:/login";
		}
		Integer userId = Integer.parseInt(userIdObj.toString());
		model.addAttribute("userId", userId);
		
		Integer productId1 = (Integer) session.getAttribute("productId");
		model.addAttribute("product", productId1);
		
		// カートの内容をセッションから取得
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
		List<Cart> groupedCartList = new ArrayList<>(groupedCartMap.values());

		// カート内の全個数を計算
		int totalQuantity = cartList.stream().mapToInt(Cart::getQuantity).sum();
		model.addAttribute("totalQuantity", totalQuantity);

		// カート内の全商品の合計金額（税抜き）
		BigDecimal totalPriceExcludingTax = cartList.stream()
				.map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		model.addAttribute("totalPriceExcludingTax", totalPriceExcludingTax);

		// カート内の全商品の合計金額（税込み）
		BigDecimal totalPriceIncludingTax = cartList.stream()
				.map(cartItem -> cartItem.getProduct().getTaxPrice()
						.multiply(BigDecimal.valueOf(cartItem.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		model.addAttribute("totalPriceIncludingTax", totalPriceIncludingTax);

		if (groupedCartList != null && !groupedCartList.isEmpty()) {
			model.addAttribute("cartList", groupedCartList);

			// カート内の各商品の商品名と画像パスを取得してモデルに追加
			List<String> productNames = cartList.stream()
					.map(cartItem -> cartItem.getProduct().getProductName())
					.collect(Collectors.toList());
			List<String> productImgPaths = cartList.stream()
					.map(cartItem -> cartItem.getProduct().getProductImgPath())
					.collect(Collectors.toList());
			List<Integer> productStocks = groupedCartList.stream()
					.map(cartItem -> cartItem.getProduct().getStock())
					.collect(Collectors.toList());
			BigDecimal totalAmount = groupedCartList.stream()
					.map(Cart::getTotalAmount)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			model.addAttribute("productName", productNames);
			model.addAttribute("productImgPath", productImgPaths);
			model.addAttribute("productStock", productStocks);
			model.addAttribute("totalAmount", totalAmount);
			model.addAttribute("cartMessage", "カートに商品が追加されました。");
		} else {
			model.addAttribute("cartMessage", "カートが空です。");
		}
		return "cart/cart_list";
	}

	@GetMapping("/updateCart")
	public String updateCart(HttpSession session, Model model, @RequestParam("productId") Integer productId,
			@RequestParam("hiddenQuantity") Integer hiddenQuantity) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			return "redirect:/login";
		}
		Integer userId = Integer.parseInt(userIdObj.toString());
		model.addAttribute("userId", userId);

		// カートの内容をセッションから取得
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
		List<Cart> groupedCartList = new ArrayList<>(groupedCartMap.values());

		// 商品の数量を更新
		for (Cart cartItem : cartList) {
			if (cartItem.getProduct().getProductId().equals(productId)) {
				cartItem.setQuantity(hiddenQuantity);
				break;
			}
		}

		// カート内の全個数を計算
		int totalQuantity = cartList.stream().mapToInt(Cart::getQuantity).sum();
		model.addAttribute("totalQuantity", totalQuantity);

		if (groupedCartList != null && !groupedCartList.isEmpty()) {
			model.addAttribute("cartList", groupedCartList);

			// カート内の各商品の商品名と画像パスを取得してモデルに追加
			List<String> productNames = cartList.stream()
					.map(cartItem -> cartItem.getProduct().getProductName())
					.collect(Collectors.toList());
			List<String> productImgPaths = cartList.stream()
					.map(cartItem -> cartItem.getProduct().getProductImgPath())
					.collect(Collectors.toList());
			List<Integer> productStocks = groupedCartList.stream()
					.map(cartItem -> cartItem.getProduct().getStock())
					.collect(Collectors.toList());
			BigDecimal totalAmount = groupedCartList.stream()
					.map(Cart::getTotalAmount)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			model.addAttribute("productName", productNames);
			model.addAttribute("productImgPath", productImgPaths);
			model.addAttribute("productStock", productStocks);
			model.addAttribute("totalAmount", totalAmount);
			model.addAttribute("cartMessage", "カートの商品が更新されました。");
		} else {
			model.addAttribute("cartMessage", "カートが空です。");
		}
		return "cart/cart_list";
	}

	/** レジに進む 
	 * @param session セッション
	 * @param model モデル
	 * @param productId 商品ID
	 * @param hiddenQuantity 数量
	 * @return 表示**/
	@PostMapping("cartPurchaseDetail")
	public String cartPurchaseDetail(HttpSession session, Model model,Integer productId,
			Integer hiddenQuantity) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
//
//		Integer productId1 = (Integer) session.getAttribute("productId");
//	    if (productId1 == null) {
//	        model.addAttribute("errorMessage", "商品IDが見つかりませんでした。");
//	    }
//	    model.addAttribute("productId", productId1);
//		// 商品IDから商品情報を取得
////		Product product = productService.getProductById(productId1);
////		if (product == null) {
////			model.addAttribute("errorMessage", "商品情報が見つかりませんでした。");
////		}
////		BigDecimal totalPriceIncludingTax = product.getTaxPrice().multiply(BigDecimal.valueOf(hiddenQuantity));
//		
//		// カートリストをセッションから取得
//	    Object cartListObj = session.getAttribute("cartList");
//	    List<Cart> cartList;
//	    if (cartListObj instanceof List<?>) {
//	        cartList = ((List<?>) cartListObj).stream()
//	                .filter(item -> item instanceof Cart)
//	                .map(item -> (Cart) item)
//	                .collect(Collectors.toList());
//	    } else {
//	        cartList = new ArrayList<>();
//	    }
//	    model.addAttribute("cartList", cartList);
//	    
//		// 新しいカートアイテムを作成して追加
//		Cart newCartItem = new Cart();
//		newCartItem.setProductId(productId);
////		newCartItem.setProduct(product);// Productオブジェクトを設定
//		newCartItem.setQuantity(hiddenQuantity);
////		newCartItem.setTotalAmount(totalPriceIncludingTax);
//
//		String userIdStr = (String) session.getAttribute("userId");
//		if (userIdStr == null) {
//			model.addAttribute("errorMessage", "ユーザーIDが見つかりませんでした。");
//		}
//		newCartItem.setUserId(Integer.parseInt(userIdStr)); // String型をInteger型に変換
//
//		cartList.add(newCartItem);
//		session.setAttribute("cartList", cartList);
//
//		// カートアイテムをデータベースに保存
//		cartService.saveCartItem(newCartItem);
		return "purchase/purchaseDetail";
	}

	@PostMapping("/deleteItem")
	@Transactional
	public String deleteItem(HttpSession session, Model model, @RequestParam("productId") Integer productId) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			return "redirect:/login";
		}
		Integer userId = Integer.parseInt(userIdObj.toString());
		model.addAttribute("userId", userId);

		// カートの内容をセッションから取得
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

		// 商品をカートから削除
		cartList.removeIf(cartItem -> cartItem.getProduct().getProductId().equals(productId));

		// カート内の全個数を計算
		int totalQuantity = cartList.stream().mapToInt(Cart::getQuantity).sum();
		model.addAttribute("totalQuantity", totalQuantity);

		// カートリストを更新
		session.setAttribute("cartList", cartList);

		return "redirect:/cartList";
	}
}
