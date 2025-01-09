package jp.co.sss.shop.service;


import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Product;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.repository.ProductListRepository;
import lombok.RequiredArgsConstructor;

/**
 * 商品検索画面 service いるか？
 */
@Service
@RequiredArgsConstructor
public class ProductService {
	
	/**	商品情報テーブルDAO */
	private final ProductListRepository productRepository;
	
	/**	カテゴリー情報テーブルDAO */
	private final CategoryRepository categoryRepository;
	
	/**
	 * 商品検索
	 * @return 表示
	 */
	public List<Product> findAllProducts() {
		return productRepository.findAll();
	}
	
	/**
	 * 商品カテゴリー検索
	 * @return 表示
	 */
	public List<Category> findAllCategories() {
		return categoryRepository.findAll();
	}
	
	/**
	 * 商品情報テーブル 商品名・カテゴリーID取得
	 * 
	 * @param productName 商品名
	 * @param categoryId カテゴリーID
	 * @return 表示
	 */
	public List<Product> findProductNameAndCategory(String productName, Integer categoryId) {
		if (categoryId != null && productName != null) {
			return productRepository.findByProductNameContainingAndCategoryId(productName, categoryId);
		} else if (productName != null) {
			return productRepository.findByProductNameContaining(productName);
		} else if (categoryId != null) {
			return productRepository.findByCategoryId(categoryId);
		} else {
			return productRepository.findAll();
		}
	}
}
