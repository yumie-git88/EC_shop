package jp.co.sss.shop.service;


import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.sss.shop.entity.SaleItem;
import jp.co.sss.shop.repository.ProductListRepository;
import jp.co.sss.shop.repository.SaleItemRepository;
import lombok.RequiredArgsConstructor;

/**
 * セール商品画面 service
 */
@Service
@RequiredArgsConstructor
public class SaleItemService {
	
	/**	セール商品情報テーブルDAO */
	private final SaleItemRepository saleItemRepository;
	
	/**	商品情報テーブルDAO */
	private final ProductListRepository productRepository;

	/**
	 * セール商品情報テーブル 商品ID取得
	 * 
	 * @param productId 商品ID
	 * @return 表示
	 */
	public List<SaleItem> getSaleItemsByProductId(Integer productId) {
		productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
		return saleItemRepository.findByProductId(productId);
	}
}
