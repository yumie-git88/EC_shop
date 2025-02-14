package jp.co.sss.shop.form;

import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import jp.co.sss.shop.entity.Product;
import lombok.Data;

/**
* 	購入品詳細画面 form
*/
@Data
public class PurchaseDetailForm {
	
	/** ユーザー住所 */
	@Null
	@Size(max = 255)
	private String userAddress1;
	
	/** アパート名等 */
	@Size(max = 255)
	private String apartment1;
	
	/** カード番号 */
	@Size(max = 255)
	private String cardNumber1;
	
	@Size(max = 255)
	private String cardNumber2;
	
	/** 有効期限 */
	@Size(max = 255)
	private String expirationDate1;
	
	@Size(max = 255)
	private String expirationDate2;
	
	/** 職場住所 */
	@Size(max = 255)
	private String userAddress2;
	
	/** 職場アパート名等 */
	@Size(max = 255)
	private String apartment2;
	
	/** 商品 */
	private Product product;
	
	private String selectedLocation;
	
	private String selectedUserAddress;
	
	private String selectedApartment;
	
	private String selectedPayInformation;
	
	private String selectedCardNumber;
	
	private String selectedExpirationDate;
}
