package jp.co.sss.shop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.shop.entity.Cart;
import jp.co.sss.shop.repository.CartRepository;
import lombok.RequiredArgsConstructor;

/**
 * カート管理 @Service
 */
@Service
@RequiredArgsConstructor
public class CartService {

	@Autowired
	private final CartRepository cartRepository;

	/** ユーザーのセッションIDに対応するカートオブジェクトを保存 **/
	private Map<String, Cart> sessionCartMap = new HashMap<>();

	//セッションIDに関連付けられたカート取得
	public Cart getCartBySession(String sessionId) {
		return sessionCartMap.computeIfAbsent(sessionId, k -> createNewCart());
	}

	private Cart createNewCart() {
		Cart cart = new Cart();
		cart.setQuantity(1); // 初期の数量を設定
		return cart;
	}

	// カートに商品を追加 
	public void addToCart(String sessionId, Cart cart) {
		sessionCartMap.put(sessionId, cart);
	}

	// ユーザーIDに基づいてカートを取得するメソッドを実装
	public List<Cart> getCartByUserId(Integer userId) {
		return cartRepository.findByUserId(userId);
	}

	public void saveCartItem(Cart cart) {
		if (cart.getProductId() == null) {
	        throw new IllegalArgumentException("Product ID must not be null");
		}
		cartRepository.save(cart);
	}
}
