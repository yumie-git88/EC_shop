package jp.co.sss.shop.form;

import lombok.Data;

/**
*
* レビュー登録画面 form
* 
*/
@Data
public class ReviewRegisterForm {
	
	private String userId;
	private Integer productId;
	
	/** ユーザー名 */
    private String dummyUserName;
	
	/** 評価（1〜5） */
    private Integer rating = 1;
	
	/** メールアドレス */
    private String email;
	
	/** コメント */
    private String comment;
	
	/** 商品画像 */
	private String reviewImgPath;
	
}
