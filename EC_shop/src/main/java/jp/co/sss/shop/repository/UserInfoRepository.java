package jp.co.sss.shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.UserInfo;

/**
 * ユーザー情報テーブルDAO
 */
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String>{
	Optional<UserInfo> findByEmail(String email);

	Optional<UserInfo> findUserByEmail(String email);
}
