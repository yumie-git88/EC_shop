package jp.co.sss.shop.entity;

import java.time.LocalDateTime;

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
@Table(name = "sales_items")
@Data
public class SaleItem {
	
	/** セール商品ID */
	@Id
	@JoinColumn(name = "sale_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer saleItemId;
	
	/** 商品ID */
	@JoinColumn(name = "product_id")
	private Integer productId;
	
	/** メーカーID */
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company companyId;
	
	/** セール名 */
	@Column(name = "sale_name")
	private String saleName;
	
	/** 説明 */
	@Column
	private String description;
	
	/** 割引率 */
	@Column(name = "discount_rate")
	private String discountRate;
	
	/** 商品画像URL */
	@Column(name = "sales_img_path")
	private String salesImgPath;
	
	/** 開始月 */
	@Column(name = "start_month")
	private String startMonth;
	
	/** 終了月 */
	@Column(name = "end_month")
	private String endMonth;
	
	/** 作成日時 */
	@Column(name = "created_at")
	private LocalDateTime createdTime;
	
	/** 更新日時 */
	@Column(name = "updated_at")
	private LocalDateTime updatedTime;
}
