package jp.co.sss.shop.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import jp.co.sss.shop.service.ProductService;
import lombok.Data;

@Entity
@Table(name = "carts")
@Data
public class Cart {

	/** カートID */
	@Id
	@Column(name = "cart_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartId;

	/** ユーザーID */
	@Column(name = "user_id")
	private Integer userId;

	/** 商品ID */
	//	@ManyToOne
	@Column(name = "product_id")
	private Integer productId;

	/** 数量 */
	@Column(name = "quantity")
	private Integer quantity;

	/** 作成日時 */
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	/** 更新日時 */
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Transient
	private List<OrderItem> orderItems = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;

	/** カート内の商品リスト（仮想プロパティ） **/
	@Transient
	private List<Cart> items = new ArrayList<>();

	@Transient
	private int totalQuantity;

	@Transient
	private ProductService productService;

	// カートアイテムを追加するメソッド  BigDecimal price
	public void addProduct(ProductService productService, Integer productId, int quantity) {
		this.productService = productService;
		boolean found = false;
		for (Cart item : items) {
			if (item.getProductId().equals(productId)) {
				item.setQuantity(item.getQuantity() + quantity);
				found = true;
				break;
			}
		}
		if (!found) {
			Cart newItem = new Cart();
			newItem.setProductId(productId);
			newItem.setQuantity(quantity);
			items.add(newItem);
		}
		this.totalQuantity = items.stream().mapToInt(Cart::getQuantity).sum(); // 合計数量を更新
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public List<Cart> getItems() {
		return items;
	}

	@Transient
	private BigDecimal totalAmount;

	public BigDecimal getTotalAmount() {
		if (productId != null && quantity != null) {
			return product.getPrice().multiply(BigDecimal.valueOf(quantity));
		}
		return BigDecimal.ZERO;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	/** CartクラスにOrderItemのリストを追加し
	 *   @return Orderクラスに変換 **/
	public Order toOrder() {
		Order order = new Order();
		order.setUserId(this.userId);
		order.setTotalAmount(this.calculateTotalAmount());
		order.setStatus("新規");
		order.setItems(this.orderItems);
		this.quantity = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
		return order;
	}

	public BigDecimal calculateTotalAmount() {
		return orderItems.stream()
				.map(OrderItem::getPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
