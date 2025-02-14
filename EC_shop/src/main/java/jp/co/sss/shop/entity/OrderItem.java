package jp.co.sss.shop.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
	
	/** オーダー商品ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Integer orderItemId;
	
	/** オーダーID */
	@Column(name = "order_id")
	private Integer orderId;
	
	/** 商品ID */
	@Column(name = "product_id")
	private Integer productId;
	
	/** 数量 */
	@Column(name = "quantity")
	private Integer quantity;
	
	/** 価格 */
	@Column(name = "price")
	private BigDecimal price;
	
	/** 追加 **/
	@ManyToOne
	@JoinColumn(name = "order_id", insertable = false, updatable = false)
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;
	
	// GetterおよびSetterメソッド
	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
}
