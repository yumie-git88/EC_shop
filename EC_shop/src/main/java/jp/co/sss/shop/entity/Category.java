package jp.co.sss.shop.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {

	/** カテゴリーID */
	@Id
	@Column(name = "category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoryId;
	
	/** カテゴリー名 */
	@Column(name = "category_name")
	private String categoryName;
	
	/** 説明 */
	@Column
	private String description;
	
	/** 親ID */
	@Column(name = "parent_id")
	private Integer parent_id;
	
	/** 作成日時 */
	@Column(name = "created_at")
	private LocalDateTime createdTime;
	
	/** 更新日時 */
	@Column(name = "updated_at")
	private LocalDateTime updatedTime;
}
