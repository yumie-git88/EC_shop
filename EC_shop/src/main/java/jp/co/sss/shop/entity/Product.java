package jp.co.sss.shop.entity;

import java.math.BigDecimal;
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

/**
 * 商品テーブル Entity
 * 
 */
@Entity
@Table(name = "products")
@Data
public class Product {
	
	/** 商品ID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;
	
	/** 商品名 */
	@Column(name = "product_name")
	private String productName;
	
	/** 価格 */
	@Column
	private BigDecimal price;
	
	/** 税込価格 */
	@Column(name = "tax_price")
	private BigDecimal taxPrice;
	
	/** 在庫数 */
	@Column
	private int stock;
	
	/** コメント */
	@Column
	private String comment;
	
	/** 画像URL */
	@Column(name = "img_path")
	private String productImgPath;
	
	/** メーカーID */ //外部参照関係にあるテーブルの結合
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company companyId;
	
	/** カテゴリーID */
//	@ManyToOne
	@JoinColumn(name = "category_id")
	private Integer categoryId;
	
	/** 税込/税抜き */
	@Column(name = "include_tax")
	private Boolean includeTax;
	
	/** 作成日時 */
	@Column(name = "created_at")
	private LocalDateTime createdTime;
	
	/** 更新日時 */
	@Column(name = "updated_at")
	private LocalDateTime updatedTime;
	
}
