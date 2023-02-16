package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdateTranCodeByProdAction extends Action {
	
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int prodNo = Integer.parseInt(request.getParameter("prodNo")); //상품번호
		String tranCode = request.getParameter("tranCode"); //2가 들어옴 2로 변경하라는 뜻(배송중)
		System.out.println("prdNo와 tranCode는??? = "+prodNo+"와"+tranCode);
		
		Purchase purchase = new Purchase();
		purchase.setPurchaseProd(new ProductServiceImpl().getProduct(prodNo)); 
		purchase.setTranCode(tranCode);
		
		PurchaseService service = new PurchaseServiceImpl();

		service.updateTranCode(purchase); 
		
		System.out.println("tranCode 변경완료");
		
		
		return "forward:/listProduct.do?menu=manage";	
	}
}
