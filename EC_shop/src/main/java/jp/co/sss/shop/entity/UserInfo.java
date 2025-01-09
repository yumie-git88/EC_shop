package jp.co.sss.shop.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * ユーザー情報テーブル Entity
 * 
 */
@Entity
@Table(name = "users")
public class UserInfo {
	
	/** ユーザーID */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private String userId;
	
	/** ユーザー名 */
	@Column(name = "user_name")
	private String userName;
	
	/** メールアドレス */
	@Column
	private String email;
	
	/** 電話番号 */
	@Column
	private String phone;
	
	/** パスワード */
	@Column(name = "passwords")
	private String password;
	
	/** 作成日時 */
	@Column(name = "created_at")
	private LocalDateTime createdTime;
	
	/** 更新日時 */
	@Column(name = "updated_at")
	private LocalDateTime updatedTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}
	
}
