package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	List<Review> findByProductId(Integer productId);
}
