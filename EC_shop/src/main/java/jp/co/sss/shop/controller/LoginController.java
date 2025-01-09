package jp.co.sss.shop.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jp.co.sss.shop.form.LoginForm;
import jp.co.sss.shop.service.LoginService;
import jp.co.sss.shop.util.AppUtil;
import lombok.RequiredArgsConstructor;

/**
 * ログイン画面 Controller
 * 
 */
@Controller
@RequiredArgsConstructor
public class LoginController {
	
	/** ログイン画面service */
	private final LoginService service;
	
	/** passwordEncoder */
	private final PasswordEncoder passwordEncoder;
	
	/** メッセージソース */
	private final MessageSource messageSource;
	
	/**
	 * 初期表示
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @return 表示画面
	 */
	@GetMapping("/login")
	public String login(Model model, LoginForm form) {
		return "login"; // login.htmlに遷移
	}
	
	/**
	 * ログイン
	 * 
	 * @param model モデル
	 * @param form 入力情報
	 * @param bdResult 入力チェック結果
	 * @param session セッションにユーザー情報を保存
	 * @param userName ユーザー名保存
	 * @return 表示画面
	 */
	@PostMapping("/login")
	public String loginForm(Model model,@Validated LoginForm form, BindingResult bdResult,
			HttpSession session, Object userName){
		if (bdResult.hasErrors()) {
			return "/login";
		}
		
		var userInfo = service.searchUserByEmail(form.getEmail());
		var isCorrectUserAuth = userInfo.isPresent()
				&& passwordEncoder.matches(form.getPassword(), userInfo.get().getPassword());
		if (isCorrectUserAuth) {
			session.setAttribute("userId", userInfo.get().getUserId()); //userIDを保存
			session.setAttribute("userName", userInfo.get().getUserName()); //ユーザー名をセッションに保存
			return "redirect:/";
		} else {
			var errorMessage = AppUtil.getMessage(messageSource, "login.wrongInput");
			model.addAttribute("ｍessage",errorMessage);
			return "login";
		}
	}
	
	/**
	 *ログアウト
	 * 
	 * @param session セッション
	 * @return リダイレクト表示画面
	 */
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}
