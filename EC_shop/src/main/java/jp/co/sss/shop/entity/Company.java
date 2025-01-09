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
@Table(name = "companies")
@Data
public class Company {

	/** メーカーID */
	@Id
	@JoinColumn(name = "company_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer companyId;
	
	/** メーカー名 */
	@Column(name = "company_name")
	private String companyName;
	
	/** 住所 */
	@Column(name = "street_address")
	private String streetAddress;
	
	/** 代表者名 */
	@Column(name = "representative_name")
	private String representativeName;
	
	/** 作成日時 */
	@Column(name = "created_at")
	private LocalDateTime createdTime;
	
	/** 更新日時 */
	@Column(name = "updated_at")
	private LocalDateTime updatedTime;
}
