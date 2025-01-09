package jp.co.sss.shop.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import jp.co.sss.shop.entity.UserInfo;
import jp.co.sss.shop.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;

/**
 * ログイン画面 service
 */
@Service
@RequiredArgsConstructor
public class LoginService {
	
	/**	ユーザー情報テーブルDAO */
	private final UserInfoRepository repository;
	
	/**
	 * ユーザー情報メールアドレス検索
	 * 
	 * @param email メールアドレス
	 * @return ユーザー情報テーブルをメールアドレス検索した結果(1件)
	 */
	public Optional<UserInfo> searchUserByEmail(String email) {
		return repository.findByEmail(email);
	}
}
