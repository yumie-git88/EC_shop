package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Product;

@Repository
public interface ProductListRepository extends JpaRepository<Product, Integer> {

	List<Product> findByProductNameContainingAndCategoryId(String productName, Integer categoryId);

	List<Product> findByProductNameContaining(String productName);

	List<Product> findByCategoryId(Integer categoryId);
}
