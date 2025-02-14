package jp.co.sss.shop.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.shop.entity.Cart;
import jp.co.sss.shop.entity.Product;
import jp.co.sss.shop.form.PurchaseDetailForm;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 *  購入品詳細画面 住所入力 Controller
 *  
 *  **/
@Controller
@RequiredArgsConstructor
public class PurchaseDetailController extends CommonController {

	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;

	/**
	 * 購入品詳細画面
	 * @param session セッション
	 * @param model モデル
	 * @param productId 商品
	 * @return 購入品詳細画面表示
	 */
	@GetMapping("/purchaseDetail")
	public String purchaseDetail(HttpSession session, Model model, Integer productId) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		if (productId == null) {
			Integer cartProductId = (Integer) session.getAttribute("productId");
			model.addAttribute("product", cartProductId);
			if (cartProductId != null) {
				Product product = productService.getProductById(cartProductId);
				model.addAttribute("product", product);
			}
		} else {
			Product product = productService.getProductById(productId);
			model.addAttribute("product", product);
		}
		return "purchase/purchaseDetail";
	}

	/**
	 * 購入確認画面へ
	 * @param form フォーム
	 * @param session セッション
	 * @param model モデル
	 * @param productId 商品ID
	 * @return 表示
	 */
	@PostMapping("/purchaseConfirmation")
	public String purchaseDetailForm(@ModelAttribute PurchaseDetailForm form,
			HttpSession session, Model model, Integer productId) {
		addCommonAttributes(session, model, productService);

		if (productId == null) {
			Integer cartProductId = (Integer) session.getAttribute("productId");
			model.addAttribute("product", cartProductId);
			if (cartProductId != null) {
				Product product = productService.getProductById(cartProductId);
				model.addAttribute("product", product);
			}
		} else {
			Product product = productService.getProductById(productId);
			model.addAttribute("product", product);
		}

		List<Cart> cartList = new ArrayList<>();
		BigDecimal totalPriceIncludingTax = BigDecimal.ZERO;
		int totalQuantity = 0;

		if (productId != null) {
			// 単品購入の場合
			Product product = productService.getProductById(productId);

			if (product == null) {
				model.addAttribute("errorMessage", "商品が見つかりません");
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
		model.addAttribute("quantity", totalQuantity);
		model.addAttribute("productStock", productStocks);
		model.addAttribute("totalAmount", totalAmount);

		String selectedLocation = form.getSelectedLocation();
		String selectedPayInformation = form.getSelectedPayInformation();

		String userAddress = selectedLocation.equals("1") ? form.getUserAddress1() : form.getUserAddress2();
		String apartment = selectedLocation.equals("1") ? form.getApartment1() : form.getApartment2();
		String cardNumber = selectedPayInformation.equals("1") ? form.getCardNumber1() : form.getCardNumber2();
		String expirationDate = selectedPayInformation.equals("1") ? form.getExpirationDate1()
				: form.getExpirationDate2();
		form.setSelectedUserAddress(userAddress);
		form.setSelectedApartment(apartment);
		form.setSelectedCardNumber(cardNumber);
		form.setSelectedExpirationDate(expirationDate);

		session.setAttribute("selectedUserAddress", userAddress);
		session.setAttribute("selectedApartment", apartment);
		session.setAttribute("selectedCardNumber", cardNumber);
		session.setAttribute("selectedExpirationDate", expirationDate);
		session.setAttribute("purchaseDetailForm", form);
		model.addAttribute("purchaseDetailForm", form);
		return "purchase/purchaseConfirmation";
	}

}
