package jp.co.sss.shop.controller;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.shop.form.UserRegisterForm;
import jp.co.sss.shop.service.UserRegisterService;
import jp.co.sss.shop.util.AppUtil;
import lombok.RequiredArgsConstructor;

/**
 * ユーザー新規登録画面 Controller
 * 
 */
@Controller
@RequiredArgsConstructor
public class UserRegisterController {
	
	/** ユーザー登録画面service */
	private final UserRegisterService service;
	
	/** メッセージソース */
	private final MessageSource messageSource;
	
	/**
	 * 初期表示
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @return 表示画面
	 */
	@GetMapping("/userRegister")
	public String userRegister(Model model, UserRegisterForm form) {
		return "userRegister";
	}
	
	/**
	 * ユーザー登録
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @param bdResult 入力チェック結果
	 */
	@PostMapping("/userRegister")
	public void userRegisterForm(Model model,@Validated UserRegisterForm form, BindingResult bdResult){
		if (bdResult.hasErrors()) {
			return;
		}
		var userInfoOpt = service.registUserInfo(form);
		if (userInfoOpt.isEmpty()) {
			var errorMessage = AppUtil.getMessage(messageSource, "register.exsitedEmail");
			model.addAttribute("ｍessage",errorMessage);
		} else {
			var messageSucceed = AppUtil.getMessage(messageSource, "register.registSucceed");
			model.addAttribute("Succeed",messageSucceed); 
		}
	}
}
