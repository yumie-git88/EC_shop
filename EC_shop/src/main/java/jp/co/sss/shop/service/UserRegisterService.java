package jp.co.sss.shop.service;

import java.util.Optional;

import org.dozer.Mapper; //選び直し
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.sss.shop.entity.UserInfo;
import jp.co.sss.shop.form.UserRegisterForm;
import jp.co.sss.shop.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;

/**
 * ユーザー新規登録画面 service
 */
@Service
@RequiredArgsConstructor
public class UserRegisterService {

	/**	ユーザー情報テーブルDAO */
	@Autowired
	private final UserInfoRepository repository;

	/** Dozer Mapper */
	private final Mapper mapper;

	/** passwordEncoder */
	private final PasswordEncoder passwordEncoder;

	/**
	 * ユーザー情報テーブル 新規登録
	 * 
	 * @param form 入力情報
	 * @return 登録情報 ユーザー情報Entity、すでにメール登録があればEmpty
	 */
	public Optional<UserInfo> registUserInfo(UserRegisterForm form) {
		var userInfoExistedOpt = repository.findByEmail(form.getEmail());
		if (userInfoExistedOpt.isPresent()) {
			return Optional.empty();
		}
		var userInfo = mapper.map(form, UserInfo.class);

		var encodedPassword = passwordEncoder.encode(form.getPassword());
		userInfo.setPassword(encodedPassword);

		return Optional.of(repository.save(userInfo));
	}

	public Optional<UserInfo> findUserByEmail(String email) {
		return repository.findByEmail(email);
	}
}
