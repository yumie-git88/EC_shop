package jp.co.sss.shop.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date; // 追加
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.entity.Cart;
import jp.co.sss.shop.entity.Product;
import jp.co.sss.shop.entity.Review;
import jp.co.sss.shop.repository.ProductListRepository;
import jp.co.sss.shop.repository.ReviewRepository;
import jp.co.sss.shop.service.CartService;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 * 商品詳細画面 Controller
 * 
 */
@Controller
@RequiredArgsConstructor
public class ProductDataController extends CommonController {

	/**	商品情報テーブルDAO */
	private final ProductListRepository repository;

	/**	レビュー情報テーブルDAO */
	private final ReviewRepository reviewRepository;

	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;

	private final CartService cartService;

	/**
	 * 商品詳細表示
	 * @param session 共通の属性を追加
	 * @param id 商品ID取得
	 * @param model モデル
	 * @param productId 商品ID
	 * @param errorMessage エラーメッセージ
	 * @return 商品詳細画面
	 */
	@GetMapping("/product/{id}")
	public String productData(HttpSession session, @PathVariable("id") Integer id, Model model, Integer productId,
			@RequestParam(value = "errorMessage", required = false) String errorMessage) {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		model.addAttribute("products", productService.findAllProducts());
		var product = repository.findById(id);
		if (product.isPresent()) {
			model.addAttribute("product", product.get());

			var reviews = reviewRepository.findByProductId(id);
			List<ReviewDTO> reviewDTOs = new ArrayList<>();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日 HH:mm");
			for (Review review : reviews) {
				Date createdTime = review.getCreatedTime(); // DateからInstantへの変換
				Instant instant = createdTime.toInstant(); // InstantからLocalDateTimeへの変換
				LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				String formattedDate = dateTime.format(formatter);
				reviewDTOs.add(new ReviewDTO(review, formattedDate));
			}

			model.addAttribute("reviews", reviewDTOs);

			if (errorMessage != null) {
				model.addAttribute("errorMessage", errorMessage);
			}
			return "product/productDetail";
		} else {
			return "redirect:/";
		}
	}

	public class ReviewDTO {
		private Review review;
		private String formattedDate;

		public ReviewDTO(Review review, String formattedDate) {
			this.review = review;
			this.formattedDate = formattedDate;
		}

		public Review getReview() {
			return review;
		}

		public String getFormattedDate() {
			return formattedDate;
		}

		public String getDummyUserName() {
			return review.getDummyUserName();
		}

		public String getComment() {
			return review.getComment();
		}

		public int getRating() {
			return review.getRating();
		}

		public Date getCreatedTime() {
			return review.getCreatedTime();
		}
	}

	/**
	 * 商品単体購入ボタン 購入品詳細画面へ
	 * 
	 * @param productId 商品ID
	 * @param session 共通の属性を追加
	 * @param model モデル
	 * @return 表示
	 */
	@PostMapping("/singlePurchaseDetail")
	public String purchaseDetailForm(@RequestParam("productId") Integer productId,
			HttpSession session, Model model) {
		addCommonAttributes(session, model, productService); // 商品IDから商品情報を取得
		Product product = productService.getProductById(productId);
		model.addAttribute("product", product);
		BigDecimal totalPriceIncludingTax = product.getTaxPrice();
		Integer productId1 = product.getProductId();
		int totalQuantity = 1;

		String userIdStr = (String) session.getAttribute("userId");
		if (userIdStr == null) {
			model.addAttribute("errorMessage", "ユーザーIDが見つかりませんでした。");
		}

		session.setAttribute("totalQuantity", totalQuantity);
		session.setAttribute("totalPriceIncludingTax", totalPriceIncludingTax);
		model.addAttribute("totalQuantity", totalQuantity);
		model.addAttribute("totalPriceIncludingTax", totalPriceIncludingTax);

		// カートに商品を追加
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

		// 新しいカートアイテムを作成して追加
		Cart newCartItem = new Cart();
		newCartItem.setProduct(product);
		newCartItem.setQuantity(totalQuantity);
		newCartItem.setTotalAmount(totalPriceIncludingTax);
		newCartItem.setProductId(productId1);
		newCartItem.setUserId(Integer.parseInt(userIdStr));

		cartList.add(newCartItem);
		session.setAttribute("cartList", cartList);

		// カートアイテムをデータベースに保存
		cartService.saveCartItem(newCartItem);
		return "purchase/purchaseDetail";
	}

	/**
	 * 口コミ投稿ボタン
	 * @param productId 商品ID
	 * @param session セッション
	 * @param model モデル
	 * @return 表示
	 */
	@PostMapping("review_post")
	public String reviewPost(@RequestParam("productId") Integer productId,
			HttpSession session, Model model) {
		addCommonAttributes(session, model, productService); // 商品IDから商品情報を取得
		Product product = productService.getProductById(productId);
		model.addAttribute("product", product);
		return "review/reviewRegister";
	}

	/**
	 * カートに入れるボタン カート追加画面へ
	 * @param productId ID取得
	 * @param quantity 終了取得
	 * @param session 共通の属性を追加
	 * @param model モデル
	 * @return カート追加画面表示
	 * @throws UnsupportedEncodingException 
	 */
	@PostMapping("/cartAdd")
	public String cartAdd(@RequestParam("productId") Integer productId, @RequestParam("quantity") Integer quantity,
			HttpSession session, Model model) throws UnsupportedEncodingException {
		addCommonAttributes(session, model, productService); // 共通の属性を追加
		Object userIdObj = session.getAttribute("userId");
		if (userIdObj == null) {
			return "redirect:/login";
		}
		try {
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
			Cart cartItem = new Cart();
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cartItem.calculateTotalAmount();
			cartList.add(cartItem);

			session.setAttribute("cartList", cartList);
			model.addAttribute("product", product);
			model.addAttribute("cartItem", cartItem);
			model.addAttribute("cartList", cartList);
			model.addAttribute("cartMessage", "商品が追加されました");

			// 消費税率を設定
			BigDecimal taxRate = new BigDecimal("0.10");

			// 追加された商品のみの合計金額を計算 (消費税込み)
			BigDecimal totalAmount = cartItem.getProduct().getPrice()
					.multiply(BigDecimal.ONE.add(taxRate))
					.multiply(BigDecimal.valueOf(cartItem.getQuantity()))
					.setScale(0, RoundingMode.DOWN); // 小数点以下を切り捨て
			model.addAttribute("totalAmount", totalAmount);

			return "cart/cart_add";
		} catch (NumberFormatException e) {
			String errorMessage = URLEncoder.encode("ユーザーIDの形式が正しくありません", StandardCharsets.UTF_8.toString());
			return "redirect:/product/" + productId + "?errorMessage=" + errorMessage;
		} catch (IllegalStateException e) {
			model.addAttribute("productId", productId);
			String errorMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8.toString());
			return "redirect:/product/" + productId + "?errorMessage=" + errorMessage;
		}
	}
}
