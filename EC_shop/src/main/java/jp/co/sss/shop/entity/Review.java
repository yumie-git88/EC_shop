package jp.co.sss.shop.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
	private Integer userId;
	
	/** 商品ID */
	@JoinColumn(name = "product_id")
	private Integer productId;
	
	/** 評価表示 */
	@Column
	@NotNull
	@Min(1)
	@Max(5)
	private int rating;
	
	/** コメント */
	@Column
	@NotBlank
	@Size(max = 300)
	private String comment = "";
	
	/** 商品ID */
	@Column(name = "dummy_user_name")
	@NotBlank
	private String dummyUserName;
	
	/** 商品画像 */
	@Column(name = "review_img_path")
	private String reviewImgPath;
	
	/** 作成日時 */
	@Column(name = "created_at")
	private Date createdTime;
	
	/** 更新日時 */
	@Column(name = "updated_at")
	private LocalDateTime updatedTime;
	
}
