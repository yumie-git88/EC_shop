package jp.co.sss.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PurchaseDetailController extends CommonController {
	
	@GetMapping("/purchaseDetail")
	public void purchaseDetail(Model model) {
		
		return;
	}
}
