package jp.co.sss.shop.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "reviews")
@Data
public class Review {
	
	/** レビューID */
	@Id
	@Column(name = "review_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reviewId;
	
	/** ユーザーID */
	@JoinColumn(name = "user_id")
	private String userId;
	
	/** 商品ID */
	@JoinColumn(name = "product_id")
	private Integer productId;
	
	/** 評価表示 */
	@Column
	private int rating;
	
	/** コメント */
	@Column
	private String comment;
	
	/** 商品ID */
	@Column(name = "dummy_user_name")
	private String dummyUserName;
	
	/** 商品ID */
	@Column(name = "review_img_path")
	private String reviewImgPath;
	
	/** 作成日時 */
	@Column(name = "created_at")
	private LocalDateTime createdTime;
	
	/** 更新日時 */
	@Column(name = "updated_at")
	private LocalDateTime updatedTime;
}
