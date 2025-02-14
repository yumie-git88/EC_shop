package jp.co.sss.shop.form;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 *
 * ログイン画面 form
 * 
 */
@Data
public class LoginForm {
	@NotBlank
	@Length(min = 1,max = 200)
	private String email;
	
	@NotBlank
	@Length(min = 8,max = 16)
	private String password;
}
