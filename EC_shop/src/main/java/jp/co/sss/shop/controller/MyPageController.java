package jp.co.sss.shop.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.entity.UserInfo;
import jp.co.sss.shop.repository.UserInfoRepository;
import jp.co.sss.shop.service.ProductService;
import lombok.RequiredArgsConstructor;

/**
 * マイページ Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController extends CommonController {

	/** ユーザー登録画面repository */
	private final UserInfoRepository repository;

	/**	商品カテゴリー情報テーブルDAO */
	private final ProductService productService;

	@GetMapping
	public String myPage(HttpSession session, Model model, Integer productId) {
		// ユーザー情報を取得
		String email = (String) session.getAttribute("userEmail");
		Optional<UserInfo> userOpt = repository.findByEmail(email);
		addCommonAttributes(session, model, productService); // 共通の属性を追加

		if (userOpt.isPresent()) {
			UserInfo user = userOpt.get();
			model.addAttribute("userName", user.getUserName());
			model.addAttribute("userPhoneNumber", user.getPhone());
			//		model.addAttribute("userHiragana", user.getHiragana()); //未実装
		} else {
			model.addAttribute("errorMessage", "ユーザー情報が見つかりませんでした。");
		}
		return "myPage";
	}
}
