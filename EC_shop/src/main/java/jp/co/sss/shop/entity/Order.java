package jp.co.sss.shop.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {
	
	/** カテゴリーID */
	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId;
	
	/** ユーザーID */
	@Column(name = "user_id")
	private Integer userId;
	
	/** 合計金額 **/
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	
	/** ステータス */
	@Column
	private String status;
	
	/** 作成日時 */
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	/** 更新日時 */
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	/** オーダーアイテム */
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> items;
	
	// 無引数のコンストラクタ
	public Order() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	//totalAmountを引数とするコンストラクタ
	public Order(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
}
