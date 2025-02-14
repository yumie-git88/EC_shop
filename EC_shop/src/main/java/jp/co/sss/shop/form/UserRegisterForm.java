package jp.co.sss.shop.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
*
* ユーザー新規登録画面 form
* 
*/
@Data
public class UserRegisterForm {
	
	/** ユーザー名 */
	@NotBlank
	@Length(min = 1,max = 255)
	private String userName;
	
	/** メールアドレス */
	@NotBlank
	@Length(min = 1,max = 255)
	private String email;
	
	/** 電話 */
	@Size(max = 20)
	private String phone;
	
	/** ユーザーアドレス */
	@Size(max = 255)
	private String user_address;
	
	/** パスワード */
	@NotBlank
	@Length(min = 8,max = 16)
	private String password;
	
	/** パスワード */
	@NotBlank
	@Length(min = 8,max = 16)
	private String confirmPassword;
	
}
