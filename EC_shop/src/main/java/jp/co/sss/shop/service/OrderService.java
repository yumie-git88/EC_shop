package jp.co.sss.shop.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sss.shop.entity.Cart;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.repository.CartRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	/** カートの内容を取得、注文を保存 **/
	private final CartRepository cartRepository;

	@Autowired
	private final OrderRepository orderRepository;

	@Autowired
	private final OrderItemRepository orderItemRepository;

	//カートの内容を取得しOrderを作成し、OrderItemに変換してリストに追加、OrderとOrderItemをDBに保存
	@Transactional
	public Order createOrder(HttpSession session, Integer userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);
		if (carts.isEmpty()) {
			throw new IllegalStateException("注文がありません");
		}
		Order order = new Order();
		order.setUserId(userId);
		order.setTotalAmount(calculateTotalAmount(carts));
		order.setStatus("注文受付");
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());

		List<OrderItem> orderItems = new ArrayList<>();
		for (Cart cart : carts) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(cart.getProduct());
			orderItem.setQuantity(cart.getQuantity());
			orderItem.setPrice(cart.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
			orderItems.add(orderItem);
		}
		order.setItems(orderItems);
		orderRepository.save(order);
		orderItemRepository.saveAll(orderItems); // OrderItem保存
		cartRepository.deleteAll(carts);
		session.removeAttribute("cart");
		return order;
	}

	private BigDecimal calculateTotalAmount(List<Cart> carts) {
		return carts.stream()
				.map(cart -> cart.getProduct().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	//必要なデータをOrderテーブルとOrderItemテーブルから取得
	@Transactional
	public List<OrderItem> getOrderItemsByUserId(Integer userId) {
		List<Order> orders = orderRepository.findByUserId(userId);
		if (orders.isEmpty()) {
			throw new IllegalStateException("注文がありません");
		}
		// 最新の注文を取得する場合
		Order latestOrder = orders.get(orders.size() - 1);
		return orderItemRepository.findByOrderId(latestOrder.getOrderId());
	}

	public Order saveOrder(Order order) {
		return orderRepository.save(order);
	}

	public List<OrderItem> saveOrderItems(List<OrderItem> orderItems) {
		return orderItemRepository.saveAll(orderItems);
	}

	public Order findOrderById(Integer orderId) {
		return orderRepository.findById(orderId).orElse(null);
	}
}
