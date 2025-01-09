package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {
	List<SaleItem> findByProductId(Integer productId);
}
